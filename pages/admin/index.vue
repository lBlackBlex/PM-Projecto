<template>
  <section class="section">
    <div class="columns is-mobile">
      <h1>all admin</h1>
    </div>
  </section>
</template>
<script>
export default {
  middleware: ['check-auth', 'auth'],
}
</script>
