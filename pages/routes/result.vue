<template>
  <section class="section">
    <div class="columns is-mobile">
      <b-table class="column" :data="flightsFounded">
        <b-table-column field="flightNumber" label="Flight" width="40" numeric v-slot="props">
          {{ props.row.flightNumber }} A1634
        </b-table-column>
        <b-table-column field="airline" label="Airline" v-slot="props">
          {{ props.row.airline }} American Airlines
        </b-table-column>
        <b-table-column field="etd" label="Depart" v-slot="props">
          {{ props.row.etd | time }}
        </b-table-column>
        <b-table-column field="eta" label="Arrive" v-slot="props">
          {{ props.row.eta | time }}
          <br>
          <code>Stop: Los Angeles, CA (LAX)</code>
        </b-table-column>
      </b-table>
    </div>
  </section>
</template>

<script>
export default {
  created() {
    this.flightsFounded = this.$store.getters.flightsFounded
  },
  data() {
    return {
      flightsFounded: []
    }
  }
}
</script>
