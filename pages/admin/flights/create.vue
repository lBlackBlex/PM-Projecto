<template>
  <section class="section">
    <div class="column is-mobile">
      <RouteForm
      :route="{
        date: '2019-01-28',
        etd: '2019-01-28 01:39',
        eta: '2019-01-28 04:39'
      }"
      :showButton="true"/>
    </div>
  </section>
</template>

<script>
import RouteForm from "~/components/RouteForm";

export default {
  middleware: ['check-auth', 'auth'],
  components: [
    RouteForm
  ]
}
</script>
