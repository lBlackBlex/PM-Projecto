<template>
  <section class="section">
    <div class="columns is-mobile">
      <Table
        class="column"
        :data="airlines"
        :columns="columns"
        control="Airline"/>
    </div>
  </section>
</template>
<script>
import Table from "~/components/Table";

export default {
  middleware: ['check-auth', 'auth', 'load-data'],
   components: {
    Table
  },
  data() {
    return {
      airlines: this.$store.getters.airlines,
      columns: [
        {
          field: 'id',
          label: 'ID',
        },
        {
          field: 'name',
          label: 'Name',
        },
      ]
    }
  },
  methods: {
    async removeAirlines(checkedRows, entity){
      const removedAirlines = []
      checkedRows.map(airline => {
        this.$axios.$delete(
          `http://localhost:8080/api/v1/airlines/${airline.id}`,
          {
            headers: {
              'Authorization': `${this.$store.getters.getToken}`
            }
          })
        removedAirlines.push(airline.id)
      })
      this.airlines = this.airlines.filter(e => !removedAirlines.includes(e.id))
      this.$buefy.toast.open({
        message: entity + ' deleted!',
        type: 'is-danger'
      })
    }
  }
}
</script>

