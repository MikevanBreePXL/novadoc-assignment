<script setup lang="ts">
import { Button, Panel, TieredMenu } from 'primevue';
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { api } from '../api/client';
import { useDocuments } from '../queries/documents';

type DocumentTypeResponse = {
  id?: number;
  name?: string;
  type?: string;
  metadataFields?: Array<{
    name?: string;
    type?: string;
  }>;
};

type ArchiveResponse = {
  id?: number;
  name?: string;
  documentClassId?: number;
  typeId?: number;
  metadata?: Array<{
    name?: string;
    type?: string;
    content?: string | number | boolean | Date;
    value?: string | number | boolean | Date;
  }>;
  metadataFields?: Array<{
    name?: string;
    type?: string;
    content?: string | number | boolean | Date;
    value?: string | number | boolean | Date;
  }>;
  [key: string]: unknown;
};

type TieredMenuItem = {
  label: string;
  icon?: string;
  items?: TieredMenuItem[];
  command?: () => void;
};

const router = useRouter();
const documentsQuery = useDocuments();

const selectedDocumentType = ref<DocumentTypeResponse | null>(null);
const selectedArchive = ref<ArchiveResponse | null>(null);
const archivesByTypeId = ref<Record<number, ArchiveResponse[]>>({});
const loadingArchivesByTypeId = ref<Record<number, boolean>>({});

const getDocumentTypeIcon = (documentType: DocumentTypeResponse) =>
  documentType.type === 'FILE' ? 'pi pi-file' : 'pi pi-folder';

const loadArchivesForType = async (documentType: DocumentTypeResponse) => {
  const typeId = documentType.id;

  if (typeId === undefined || typeId === null) {
    return;
  }

  if (archivesByTypeId.value[typeId] || loadingArchivesByTypeId.value[typeId]) {
    return;
  }

  loadingArchivesByTypeId.value = {
    ...loadingArchivesByTypeId.value,
    [typeId]: true,
  };

  try {
    const archiveResponse = await api.get('/archive', {
      params: { typeId },
    });

    archivesByTypeId.value = {
      ...archivesByTypeId.value,
      [typeId]: Array.isArray(archiveResponse.data)
        ? (archiveResponse.data as ArchiveResponse[])
        : [],
    };
  } finally {
    loadingArchivesByTypeId.value = {
      ...loadingArchivesByTypeId.value,
      [typeId]: false,
    };
  }
};

const itemTypes = computed<TieredMenuItem[]>(() => {
  const documentTypes = Array.isArray(documentsQuery.data.value)
    ? (documentsQuery.data.value as DocumentTypeResponse[])
    : [];

  return documentTypes.map((documentType) => {
    const typeId = documentType.id;
    const archives =
      typeId === undefined || typeId === null
        ? []
        : archivesByTypeId.value[typeId] ?? [];

    return {
      label: documentType.name ?? '',
      icon: getDocumentTypeIcon(documentType),
      command: () => {
        selectedDocumentType.value = documentType;
        selectedArchive.value = null;
        void loadArchivesForType(documentType);
      },
      items: archives.map((archive) => ({
        label: archive.name ?? `Archive ${archive.id ?? ''}`,
        icon: 'pi pi-file',
        command: () => {
          selectedDocumentType.value = documentType;
          selectedArchive.value = archive;
        },
      })),
    };
  });
});

const selectedDetails = computed(() => selectedArchive.value ?? selectedDocumentType.value);

const selectedMetadataFields = computed(() => {
  const details = selectedDetails.value;

  if (!details || !('metadataFields' in details)) {
    return [];
  }

  return details.metadataFields ?? [];
});

const selectedArchiveMetadata = computed(() => {
  const archive = selectedArchive.value;

  if (!archive) {
    return [] as Array<{ name: string; type?: string; value: unknown }>;
  }

  const rawMetadata = archive.metadata ?? archive.metadataFields ?? [];

  return rawMetadata
    .filter(Boolean)
    .map((field) => ({
      name: field.name ?? 'Metadata',
      type: field.type,
      value: field.content ?? field.value ?? '',
    }));
});

const hasSelectedDetails = computed(() => selectedDetails.value !== null);

const formatValue = (value: unknown) => {
  if (value instanceof Date) {
    return value.toISOString();
  }

  if (typeof value === 'object' && value !== null) {
    return JSON.stringify(value, null, 2);
  }

  return String(value);
};

const defineNewDocumentTypeAction = () => {
  router.push('/define');
};
</script>

<template>
  <div class="dashboard-layout">
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2 class="sidebar-title">Document types</h2>
        <Button
          v-ripple
          label="Create"
          icon="pi pi-plus"
          @click="defineNewDocumentTypeAction"
        />
      </div>

      <TieredMenu
        v-if="itemTypes.length > 0"
        class="sidebar-menu"
        :model="itemTypes"
      />

      <Panel v-else class="panel">
        <template #header>
          <span class="font-bold">Geen document types gedefinieerd</span>
        </template>
        <p>
          Beheer de document types die gebruikt kunnen worden om een nieuw
          document te maken.
        </p>
      </Panel>
    </aside>

    <main class="main-content">
      <Panel class="panel main-panel">
        <template #header>
          <span class="font-bold">Geselecteerde categorie</span>
        </template>

        <div v-if="hasSelectedDetails" class="document-details">
          <p class="document-name">
            {{ selectedArchive?.name ?? selectedDocumentType?.name }}
          </p>

          <dl class="details-grid">
            <template v-if="selectedDetails?.id !== undefined && selectedDetails?.id !== null">
              <dt>Id</dt>
              <dd>{{ selectedDetails.id }}</dd>
            </template>
            <template v-if="selectedDetails && 'type' in selectedDetails && selectedDetails.type">
              <dt>Type</dt>
              <dd>{{ selectedDetails.type }}</dd>
            </template>
          </dl>

          <div v-if="selectedArchive" class="metadata-block">
            <h3 class="metadata-title">Archive metadata</h3>
            <ul v-if="selectedArchiveMetadata.length" class="metadata-list archive-metadata-list">
              <li
                v-for="field in selectedArchiveMetadata"
                :key="`${field.name}-${field.type ?? 'type'}-${formatValue(field.value)}`"
              >
                <span class="metadata-name">{{ field.name }}</span>
                <span v-if="field.type" class="metadata-type">{{ field.type }}</span>
                <span class="metadata-value">{{ formatValue(field.value) }}</span>
              </li>
            </ul>
            <p v-else>Geen archiefmetadata beschikbaar voor dit item.</p>
          </div>

          <div v-else class="metadata-block">
            <h3 class="metadata-title">Metadata fields</h3>
            <ul v-if="selectedMetadataFields.length" class="metadata-list">
              <li
                v-for="field in selectedMetadataFields"
                :key="`${field.name ?? 'field'}-${field.type ?? 'type'}`"
              >
                {{ field.name }} <span v-if="field.type">({{ field.type }})</span>
              </li>
            </ul>
            <p v-else>Geen metadata fields beschikbaar voor dit item.</p>
          </div>
        </div>

        <p v-else>
          Selecteer links een document type of archive om de gegevens daarvoor te tonen.
        </p>
      </Panel>
    </main>
  </div>
</template>

<style scoped>
.dashboard-layout {
  display: flex;
  gap: 1.5rem;
  min-height: 100vh;
  padding: 1.5rem;
}

.sidebar {
  width: 280px;
  flex: 0 0 280px;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.sidebar-title {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 700;
}

.sidebar-menu {
  width: 100%;
}

.main-content {
  flex: 1;
  display: flex;
  align-items: flex-start;
}

.main-panel {
  width: 100%;
}

.document-details {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.document-name {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 700;
}

.metadata-block {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.metadata-title {
  margin: 0;
  font-size: 1rem;
  font-weight: 600;
}

.metadata-list {
  margin: 0;
  padding-left: 1.25rem;
}

.archive-metadata-list {
  list-style: none;
  padding-left: 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.archive-metadata-list li {
  display: grid;
  grid-template-columns: minmax(0, 1.2fr) auto minmax(0, 1.8fr);
  gap: 0.75rem;
  align-items: start;
  padding: 0.75rem 0.9rem;
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 0.75rem;
  background: rgba(30, 30, 30, 0.65);
}

.metadata-name {
  font-weight: 600;
}

.metadata-type {
  color: #95a1a9;
  font-size: 0.9rem;
}

.metadata-value {
  justify-self: end;
  text-align: right;
  font-variant-numeric: tabular-nums;
}

.details-grid {
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 0.35rem 0.9rem;
  margin: 0;
}

.details-grid dt {
  font-weight: 600;
  color: #4b5563;
}

.details-grid dd {
  margin: 0;
}

.panel {
  width: 100%;
}

@media only screen and (max-width: 600px) {
  .dashboard-layout {
    flex-direction: column;
    padding: 1rem;
  }

  .sidebar {
    width: 95%;
    flex-basis: auto;
  }
}
</style>
