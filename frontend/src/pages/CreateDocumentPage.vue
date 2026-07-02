<script setup lang="ts">
import { useQuery } from '@tanstack/vue-query';
import {
  Checkbox,
  Button,
  FloatLabel,
  InputNumber,
  InputText,
  DatePicker,
  Panel,
  Select,
} from 'primevue';
import { computed, reactive, watch } from 'vue';
import { useRouter } from 'vue-router';

import { api } from '../api/client';

const router = useRouter();

type MetadataType = 'STRING' | 'INTEGER' | 'BOOLEAN' | 'DATE';
type FileType = 'FILE' | 'FOLDER';

type DocumentTypeMetadataField = {
  name: string;
  type: MetadataType;
};

type DocumentTypeMetadataFieldResponse = {
  Name?: string;
  Type?: MetadataType;
  name?: string;
  type?: MetadataType;
};

type DocumentTypeDefinition = {
  id?: number;
  name: string;
  fileType: FileType;
  metadataFields: DocumentTypeMetadataField[];
};

type DocumentTypeDefinitionResponse = {
  Id?: number;
  Name?: string;
  FileType?: FileType;
  Type?: FileType;
  MetadataFields?: DocumentTypeMetadataFieldResponse[];
  Metadatafields?: DocumentTypeMetadataFieldResponse | DocumentTypeMetadataFieldResponse[];
  id?: number;
  name?: string;
  fileType?: FileType;
  type?: FileType;
  metadataFields?: DocumentTypeMetadataFieldResponse | DocumentTypeMetadataFieldResponse[];
};

type MetadataValueRow = {
  id: number;
  name: string;
  type: MetadataType;
  value: string | number | boolean | Date | null;
};

type CreateDocumentPayload = {
  name: string;
  documentClassId: number | null;
  metadata: Array<{
    name: string;
    type: MetadataType;
    content: string | null;
  }>;
};

let nextMetadataFieldId = 1;

const createMetadataField = (
  field: DocumentTypeMetadataField,
): MetadataValueRow => ({
  id: nextMetadataFieldId++,
  name: field.name,
  type: field.type,
  value: null,
});

const getStringValue = (field: MetadataValueRow) =>
  typeof field.value === 'string' ? field.value : '';

const setStringValue = (field: MetadataValueRow, value: string | undefined) => {
  field.value = value ?? '';
};

const getIntegerValue = (field: MetadataValueRow) =>
  typeof field.value === 'number' ? field.value : null;

const setIntegerValue = (field: MetadataValueRow, value: number | null) => {
  field.value = value;
};

const getBooleanValue = (field: MetadataValueRow) =>
  typeof field.value === 'boolean' ? field.value : false;

const setBooleanValue = (field: MetadataValueRow, value: boolean) => {
  field.value = value;
};

const getDateValue = (field: MetadataValueRow) =>
  field.value instanceof Date ? field.value : null;

const setDateValue = (
  field: MetadataValueRow,
  value: Date | Date[] | (Date | null)[] | null | undefined,
) => {
  field.value = Array.isArray(value) ? value[0] ?? null : value ?? null;
};

const formatDate = (value: Date) => {
  const day = String(value.getDate()).padStart(2, '0');
  const month = String(value.getMonth() + 1).padStart(2, '0');
  const year = value.getFullYear();

  return `${day}-${month}-${year}`;
};

const normalizeMetadataField = (
  field: DocumentTypeMetadataFieldResponse,
): DocumentTypeMetadataField => ({
  name: field.name ?? field.Name ?? '',
  type: field.type ?? field.Type ?? 'STRING',
});

const normalizeDocumentType = (
  documentType: DocumentTypeDefinitionResponse,
): DocumentTypeDefinition => ({
  id: documentType.id ?? documentType.Id,
  name: documentType.name ?? documentType.Name ?? '',
  fileType:
    documentType.type ??
    documentType.Type ??
    'FILE',
  metadataFields: (() => {
    const rawMetadataFields =
      documentType.metadataFields ??
      documentType.MetadataFields ??
      documentType.Metadatafields ??
      [];

    const metadataFieldList = Array.isArray(rawMetadataFields)
      ? rawMetadataFields
      : [rawMetadataFields];

    return metadataFieldList
      .filter(Boolean)
      .map((field) => normalizeMetadataField(field));
  })(),
});

const documentTypesQuery = useQuery<DocumentTypeDefinition[]>({
  queryKey: ['document-types'],
  queryFn: async () => {
    const response = await api.get('/documents');
    return Array.isArray(response.data) ? response.data : [];
  },
  select: (responseData) =>
    responseData.map((documentType: DocumentTypeDefinitionResponse) =>
      normalizeDocumentType(documentType),
    ),
  staleTime: 30_000,
  refetchOnWindowFocus: false,
});

const documentTypes = computed(() => documentTypesQuery.data.value ?? []);

console.log('Document types:', documentTypes.value);

const documentTypeOptions = computed(() =>
  documentTypes.value.map((documentType) => ({
    label: `${documentType.name} (${documentType.fileType})`,
    value: documentType.name,
  })),
);

const form = reactive({
  name: '',
  documentTypeName: '',
  metadataFields: [] as MetadataValueRow[],
});

const selectedDocumentType = computed(() =>
  documentTypes.value.find(
    (documentType) => documentType.name === form.documentTypeName,
  ),
);

const syncMetadataFields = () => {
  const metadataFields = selectedDocumentType.value?.metadataFields ?? [];

  if (!metadataFields.length) {
    form.metadataFields = [];
    return;
  }

  form.metadataFields = metadataFields.map((field) => createMetadataField(field));
};

watch(
  documentTypes,
  (availableDocumentTypes) => {
    if (!availableDocumentTypes.length) {
      form.documentTypeName = '';
      form.metadataFields = [];
      return;
    }

    if (
      !form.documentTypeName ||
      !availableDocumentTypes.some(
        (documentType) => documentType.name === form.documentTypeName,
      )
    ) {
      form.documentTypeName = availableDocumentTypes[0].name;
    }

    syncMetadataFields();
  },
  { immediate: true },
);

watch(
  () => form.documentTypeName,
  () => {
    syncMetadataFields();
  },
);

const save = async () => {
  const payload: CreateDocumentPayload = {
    name: form.name.trim(),
    documentClassId: selectedDocumentType.value?.id ?? null,
    metadata: form.metadataFields.map((field) => ({
      name: field.name.trim(),
      type: field.type,
      content:
        field.type === 'DATE'
          ? field.value instanceof Date
            ? formatDate(field.value)
            : null
          : field.type === 'INTEGER'
            ? typeof field.value === 'number'
              ? String(field.value)
              : null
            : field.type === 'BOOLEAN'
              ? typeof field.value === 'boolean'
                ? String(field.value)
                : null
              : typeof field.value === 'string'
                ? field.value.trim()
                : null,
    })),
  };

  await api.post('/archive', payload);

  router.push('/');
};
</script>

<template>
  <div class="flex flex-col justify-start items-center h-screen">
    <Panel class="w-[80vw] mt-10 sm:mt-5">
      <template #header>
        <span class="font-bold">Sla een document op</span>
      </template>
      <form @submit.prevent="save">
        <div class="flex justify-center">
          <FloatLabel variant="on">
            <InputText
              id="documentName"
              v-model="form.name"
              class="w-full md:w-56"
              fluid
            />
            <label for="documentName">Naam</label>
          </FloatLabel>
        </div>
        <div class="flex justify-center my-2">
          <Select
            v-model="form.documentTypeName"
            :options="documentTypeOptions"
            option-label="label"
            option-value="value"
            placeholder="Select document type"
            class="w-full md:w-56"
          />
        </div>
        <div class="flex justify-center mt-2 text-sm text-surface-500">
          <span v-if="selectedDocumentType">
            {{ selectedDocumentType.fileType }} type with
            {{ selectedDocumentType.metadataFields?.length ?? 0 }} metadata field(s)
          </span>
        </div>
        <table
          v-if="selectedDocumentType"
          class="table-auto w-[80%] mx-[10%] mt-3"
        >
          <thead>
            <tr>
              <th>Metadata</th>
              <th>Type</th>
              <th>Value</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="field in form.metadataFields" :key="field.id">
              <td>
                <span class="w-full">{{ field.name }}</span>
              </td>

              <td>
                <span class="w-full">{{ field.type }}</span>
              </td>

              <td>
                <InputText
                  v-if="field.type === 'STRING'"
                  :model-value="getStringValue(field)"
                  @update:model-value="setStringValue(field, $event)"
                  class="w-[90%] mx-[5%]"
                  placeholder="Voer tekst in"
                />

                <InputNumber
                  v-else-if="field.type === 'INTEGER'"
                  :model-value="getIntegerValue(field)"
                  @update:model-value="setIntegerValue(field, $event)"
                  class="w-[90%] mx-[5%]"
                  :use-grouping="false"
                  placeholder="Voer nummer in"
                />

                <div
                  v-else-if="field.type === 'BOOLEAN'"
                  class="w-[90%] mx-[5%] flex items-center gap-2"
                >
                  <Checkbox
                    :model-value="getBooleanValue(field)"
                    binary
                    @update:model-value="setBooleanValue(field, $event)"
                  />
                  <span>{{ getBooleanValue(field) ? 'Ja' : 'Nee' }}</span>
                </div>

                <DatePicker
                  v-else-if="field.type === 'DATE'"
                  :model-value="getDateValue(field)"
                  @update:model-value="setDateValue(field, $event)"
                  class="w-[90%] mx-[5%]"
                  date-format="dd-MM-yyyy"
                  placeholder="Selecteer datum"
                />
              </td>
            </tr>
          </tbody>
        </table>
        <div class="mt-3 flex justify-end">
          <Button v-ripple type="submit">Save</Button>
        </div>
      </form>
    </Panel>
  </div>
</template>

<style scoped></style>
