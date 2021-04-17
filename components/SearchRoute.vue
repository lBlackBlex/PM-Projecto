<template>
  <div class="columns is-mobile">
    <b-field class="column" label="From" label-position="on-border">
      <b-input v-model="form.from"></b-input>
    </b-field>
    <b-field class="column" label="To" label-position="on-border">
      <b-input v-model="form.to"></b-input>
    </b-field>
    <b-field class="column" label="Date" label-position="on-border">
      <b-autocomplete
        v-model="form.selected"
        :data="filteredDataObj"
        field="formatted"
        open-on-focus/>
    </b-field>
    <div class="column">
      <b-button class="is-primary" @click="toSearch" icon-left="magnify">Search</b-button>
    </div>
  </div>
</template>

<script>
export default {
  created() {
    this.loadSupportedDates(2)
  },
  data() {
    return {
      supportedDates: [],
      form: {
        from: '',
        to: '',
        selected: ''
      },
    }
  },
  computed: {
    filteredDataObj() {
      return this.supportedDates.filter(option => {
        return (
          option.formatted
        )
      })
    },
  },
  methods: {
    loadSupportedDates(days) {
      const n = days * 2 + 1
      for (let i = 1; i <= n; i++) {
        let aux = new Date()
        aux.setDate(aux.getDate() - days)
        days--
        const [dayName, dayAndMonth,] = this.$root.$options.filters.date(aux).split(',')
        const date = aux.getFullYear() + "-" + (aux.getMonth() + 1) + "-" + aux.getDate()
        this.supportedDates.push({date: date, formatted: dayName + ',' + dayAndMonth})
      }
    },
    async toSearch() {
      const selected = this.supportedDates.find(obj => {
        return obj.formatted === this.form.selected
      })
      const flights = await this.$axios.$get("http://localhost:8080/api/v1/routes/", {
        params: {
          date: selected.date,
          to: this.form.to,
          from: this.form.from
        }
      })
      await this.$store.dispatch('flightsFounded', flights)
      await this.$router.push({
        path: '/routes/result',
      })
    }
  }
}
</script>
