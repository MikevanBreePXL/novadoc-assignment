import { createApp } from 'vue';
import './style.css';
import App from './App.vue';
import router from './router';
import PrimeVue from 'primevue/config';
import Lara from '@primeuix/themes/lara';
import { VueQueryPlugin } from '@tanstack/vue-query';
import { definePreset } from '@primeuix/themes';

const themePreset = definePreset(Lara, {
  semantic: {
    primary: {
      50: '{indigo:50}',
      100: '{indigo:100}',
      200: '{indigo:200}',
      300: '{indigo:300}',
      400: '{indigo:400}',
      500: '{indigo:500}',
      600: '{indigo:600}',
      700: '{indigo:700}',
      800: '{indigo:800}',
      900: '{indigo:900}',
      950: '{indigo.950}',
    },
  },
});

const app = createApp(App);

app.use(router);
app.use(VueQueryPlugin);
app.use(PrimeVue, {
  theme: {
    preset: themePreset,
  },
});

app.mount('#app');
