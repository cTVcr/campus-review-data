<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import DefaultLayout from '@/layouts/DefaultLayout.vue'

const userStore = useUserStore()

onMounted(() => {
  userStore.restoreLogin()
})
</script>

<template>
  <DefaultLayout />
</template>
