<script setup>
import { useQuery } from '@tanstack/vue-query';
import { Form } from '@primevue/forms';
import {
  Button,
  FloatLabel,
  InputText,
  Panel,
  Select,
} from 'primevue';
import { computed, reactive, ref } from 'vue';
import { api } from '../api/client';
import { useRouter } from 'vue-router';

const router = useRouter();

const normalizeMetadataField = (field) => ({
  name: field.name ?? field.Name ?? '',
  type: field.type ?? field.Type ?? 'STRING',
});

const normalizeDocumentType = (documentType) => ({
  id: documentType.id ?? documentType.Id ?? null,
  name: documentType.name ?? documentType.Name ?? '',
  fileType: documentType.fileType ?? documentType.FileType ?? documentType.type ?? documentType.Type ?? 'FILE',
  metadataFields: (() => {
    const rawMetadataFields =
      documentType.metadataFields ??
      documentType.MetadataFields ??
      documentType.Metadatafields ??
      [];

    const metadataFieldList = Array.isArray(rawMetadataFields)
      ? rawMetadataFields
      : [rawMetadataFields];

    return metadataFieldList.filter(Boolean).map((field) => normalizeMetadataField(field));
  })(),
});

const documentTypesQuery = useQuery({
  queryKey: ['document-types'],
  queryFn: async () => {
    const response = await api.get('/documents');
    return Array.isArray(response.data) ? response.data : [];
  },
  select: (responseData) =>
    responseData.map((documentType) => normalizeDocumentType(documentType)),
  staleTime: 30_000,
  refetchOnWindowFocus: false,
});

const existingDocumentTypes = computed(() => documentTypesQuery.data.value ?? []);

const deleteDocumentType = async (documentTypeId) => {
  if (!documentTypeId) {
    return;
  }

  const archiveResponse = await api.get(`/archive?typeId=${documentTypeId}`);
  const archives = Array.isArray(archiveResponse.data) ? archiveResponse.data : [];

  if (archives.length > 0) {
    window.alert('Dit document type kan niet verwijderd worden omdat er al archives aan gekoppeld zijn.');
    return; 
  }

  await api.delete(`/documents/${documentTypeId}`);
  documentTypesQuery.refetch();
};

const metadataTypes = ['STRING', 'INTEGER', 'BOOLEAN', 'DATE'];

let nextMetadataFieldId = 1;

const createMetadataField = () => ({
  id: nextMetadataFieldId++,
  name: '',
  type: 'STRING',
});

const documentTypes = ['FILE', 'FOLDER'];

const form = reactive({
  name: '',
  documentType: documentTypes[0],
  metadataFields: [createMetadataField()],
});

const validationErrors = reactive({
  name: '',
  documentType: '',
  metadataFields: [],
});

const syncMetadataFieldErrors = () => {
  validationErrors.metadataFields = form.metadataFields.map(() => ({
    name: '',
    type: '',
  }));
};

syncMetadataFieldErrors();

const validateForm = () => {
  let isValid = true;

  validationErrors.name = '';
  validationErrors.documentType = '';
  syncMetadataFieldErrors();

  if (!form.name.trim()) {
    validationErrors.name = 'Naam is verplicht.';
    isValid = false;
  }

  if (!form.documentType) {
    validationErrors.documentType = 'Document type is verplicht.';
    isValid = false;
  }

  form.metadataFields.forEach((field, index) => {
    if (!field.name.trim()) {
      validationErrors.metadataFields[index].name = 'Metadata naam is verplicht.';
      isValid = false;
    }

    if (!field.type) {
      validationErrors.metadataFields[index].type = 'Metadata type is verplicht.';
      isValid = false;
    }
  });

  return isValid;
};

const addField = () => {
  form.metadataFields.push(createMetadataField());
  syncMetadataFieldErrors();
};

const removeField = (fieldId) => {
  form.metadataFields = form.metadataFields.filter(
    (field) => field.id !== fieldId,
  );
  syncMetadataFieldErrors();
};

const save = async () => {
  if (!validateForm()) {
    return;
  }

  const payload = {
    name: form.name.trim(),
    documentType: form.documentType,
    metadataFields: form.metadataFields.map(({ id, ...field }) => ({
      name: field.name.trim(),
      type: field.type,
    })),
  };
  await api.post('/documents', payload);

  router.push('/');
};
</script>

<template>
  <div class="flex flex-col justify-start items-center h-screen">
    <div class="w-[80vw] mt-8">
      <div class="flex items-center justify-between mb-3">
        <span class="font-bold text-lg">Bestaande document types</span>
        <span class="text-sm text-surface-500">{{ existingDocumentTypes.length }} gevonden</span>
      </div>

      <div class="flex gap-3 overflow-x-auto pb-2">
        <Panel
          v-for="documentType in existingDocumentTypes"
          :key="`${documentType.id ?? documentType.name}`"
          class="min-w-[220px] shrink-0"
        >
          <template #header>
            <div class="text-sm font-semibold leading-tight">
              {{ documentType.name }}
            </div>
          </template>

          <div class="text-sm space-y-1">
            <div><span class="font-medium">Type:</span> {{ documentType.fileType }}</div>
            <div>
              <span class="font-medium">Metadata:</span>
              {{ documentType.metadataFields.length }} field(s)
            </div>
          </div>

          <template #footer>
            <div class="flex justify-end">
              <Button
                v-if="documentType.id"
                v-ripple
                severity="danger"
                variant="outlined"
                icon="pi pi-trash"
                type="button"
                @click="deleteDocumentType(documentType.id)"
              />
            </div>
          </template>
        </Panel>
      </div>
    </div>

    <Panel class="w-[80vw] mt-10 sm:mt-5">
      <template #header>
        <span class="font-bold">Definieer een nieuw document type</span>
      </template>
      <template #footer>
        <div class="flex justify-end">
          <Button v-ripple type="button" @click="save">Save</Button>
        </div>
      </template>
      <Form>
        <div class="flex justify-center">
          <FloatLabel variant="on">
            <InputText
              id="documentTypeName"
              v-model="form.name"
              :invalid="!!validationErrors.name"
              class="w-full md:w-56"
              fluid
            />
            <label for="documentTypeName">Naam</label>
          </FloatLabel>
        </div>
        <div v-if="validationErrors.name" class="flex justify-center mt-1 text-sm text-red-500">
          {{ validationErrors.name }}
        </div>
        <div class="flex justify-center my-2">
          <Select
            v-model="form.documentType"
            :options="documentTypes"
            :invalid="!!validationErrors.documentType"
            placeholder="Document/Map"
            class="w-full md:w-56"
          />
        </div>
        <div v-if="validationErrors.documentType" class="flex justify-center mt-1 text-sm text-red-500">
          {{ validationErrors.documentType }}
        </div>
        <table class="table-auto w-[80%] mx-[10%] mt-3">
          <thead>
            <tr>
              <th>Metadata</th>
              <th>Type</th>
              <th></th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="field in form.metadataFields" :key="field.id">
              <td>
                <div>
                  <InputText
                    v-model="field.name"
                    :invalid="!!validationErrors.metadataFields[form.metadataFields.indexOf(field)]?.name"
                    class="w-[90%] mx-[5%]"
                    placeholder="Naam"
                  />
                  <div
                    v-if="validationErrors.metadataFields[form.metadataFields.indexOf(field)]?.name"
                    class="mt-1 text-xs text-red-500 w-[90%] mx-[5%]"
                  >
                    {{ validationErrors.metadataFields[form.metadataFields.indexOf(field)]?.name }}
                  </div>
                </div>
              </td>

              <td>
                <div>
                  <Select
                    v-model="field.type"
                    :options="metadataTypes"
                    :invalid="!!validationErrors.metadataFields[form.metadataFields.indexOf(field)]?.type"
                    class="w-[90%] mx-[5%]"
                  />
                  <div
                    v-if="validationErrors.metadataFields[form.metadataFields.indexOf(field)]?.type"
                    class="mt-1 text-xs text-red-500 w-[90%] mx-[5%]"
                  >
                    {{ validationErrors.metadataFields[form.metadataFields.indexOf(field)]?.type }}
                  </div>
                </div>
              </td>

              <td>
                <Button
                  v-ripple
                  variant="outlined"
                  type="button"
                  icon="pi pi-trash"
                  @click="removeField(field.id)"
                />
              </td>
            </tr>
          </tbody>
        </table>
        <div class="mt-3 ml-[12%]">
          <Button v-ripple type="button" @click="addField">Add field</Button>
        </div>
      </Form>
    </Panel>
  </div>
</template>

<style scoped></style>
