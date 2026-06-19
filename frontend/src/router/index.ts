import { createRouter, createWebHistory } from 'vue-router';
import DashboardPage from '../pages/DashboardPage.vue';

const routes = [
  { path: '/', component: DashboardPage },
  { path: '/define', component: () => import('../pages/DefineNewDocumentTypePage.vue') },
  { path: '/create', component: () => import('../pages/CreateDocumentPage.vue') },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
