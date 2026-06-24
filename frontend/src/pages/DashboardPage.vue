<script setup lang="ts">
import { Button, Panel } from 'primevue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const isHovered = ref(false);
const router = useRouter();

const CreateDocumentAction = () => {
  router.push('/create');
};
const defineNewDocumentTypeAction = () => {
  router.push('/define');
};
</script>

<template>
  <div class="dashboard-page flex flex-row items-center justify-center h-full">
    <Panel @mouseenter="isHovered = true" class="panel">
      <template #header>
        <span class="font-bold">Maak een nieuw document aan</span>
      </template>
      <template #footer>
        <div class="flex flex-row align-end justify-end">
          <Button
            v-ripple
            label="Create"
            icon="pi pi-plus"
            @click="CreateDocumentAction"
          />
        </div>
      </template>
      <p>
        Digitaliseer een nieuw document, volgens een van de bestaande document
        types.
      </p>
    </Panel>
    <Panel @mouseenter="isHovered = true" class="panel">
      <template #header>
        <span class="font-bold">Definieer een nieuw document type</span>
      </template>
      <template #footer>
        <div class="flex flex-row align-end justify-end">
          <Button
            v-ripple
            label="Create"
            icon="pi pi-plus"
            @click="defineNewDocumentTypeAction"
          />
        </div>
      </template>
      <p>
        Beheer de document types die gebruikt kunnen worden om een nieuw
        document te maken.
      </p>
    </Panel>
  </div>
</template>

<style scoped>
.panel {
  width: 25%;
  transition: margin 0.2s ease-in-out;
  margin: 0 auto;
}
.panel:hover {
  transition: margin 0.2s ease-in-out;
  margin: 0 auto;
  margin-bottom: 20px;
}

@media only screen and (max-width: 600px) {
  .dashboard-page {
    padding-top: 50px;
    flex-direction: column;
    align-items: center;
    justify-content: space-evenly;
  }
  .panel {
    width: 95%;
  }
}
</style>
