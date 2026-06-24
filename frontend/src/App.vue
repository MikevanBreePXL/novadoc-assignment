<script setup lang="ts">
import { Menubar } from 'primevue';
import { RouterView } from 'vue-router';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const items = ref([
    {
        label: 'Home',
        icon: 'pi pi-home',
        command: () => {
            router.push('/');
        }
    },
    {
        label: 'New Type',
        icon: 'pi pi-plus',
        command: () => {
            router.push('/define');
        }
    },
    {
        label: 'New Document',
        icon: 'pi pi-file-plus',
        command: () => {
            router.push('/create');
        }
    }
  ]);
</script>

<template>
  <div class="card">
  <Menubar :model="items" style="padding-bottom: 0; padding-top: 0;">
    <template #item="{ item, props, hasSubmenu }" class="py-5">
        <router-link v-if="item.route" v-slot="{ href, navigate }" :to="item.route" custom>
            <a v-ripple :href="href" v-bind="props.action" @click="navigate">
                <span :class="item.icon" />
                <span>{{ item.label }}</span>
            </a>
        </router-link>
        <a v-else v-ripple :href="item.url" :target="item.target" v-bind="props.action">
            <span :class="item.icon" />
            <span>{{ item.label }}</span>
            <span v-if="hasSubmenu" class="pi pi-fw pi-angle-down" />
        </a>
    </template>
  </Menubar>
  </div>
  <RouterView />
</template>
