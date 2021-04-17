<template>
  <section class="section">
    <div class="columns is-mobile">
      <PlaneForm
        class="column"
        :plane="{}"
        :showButton="true"/>
    </div>
  </section>
</template>

<script>
import PlaneForm from "~/components/AirlineForm";

export default {
  components: [
    PlaneForm
  ],
  middleware: ['check-auth', 'auth'],
}
</script>
