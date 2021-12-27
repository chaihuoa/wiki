<template>
  <a-layout-footer style="text-align: center">
    {{user.name}}<span v-show="user.id">'s</span> Wiki.
  </a-layout-footer>
</template>

<script lang="ts">
import { defineComponent, computed, onMounted } from 'vue';
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
  setup() {
    const user = computed(() => store.state.user);

    return {
      user
    }
  }
});
</script>