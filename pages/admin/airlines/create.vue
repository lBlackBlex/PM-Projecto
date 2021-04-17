<template>
  <section class="section">
    <div class="columns is-mobile">
      <AirlineForm
        class="column"
        :airline="{}"
        :showButton="true"/>
    </div>
  </section>
</template>

<script>
import AirlineForm from "~/components/AirlineForm";

export default {
  components: [
    AirlineForm
  ],
  middleware: ['check-auth', 'auth'],
}
</script>
