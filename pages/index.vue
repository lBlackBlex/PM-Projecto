<template>
  <section class="section">
    <div class="columns is-mobile">
      <section class="column">
        <b-tabs type="is-boxed">
          <b-tab-item>
            <template #header>
              <b-icon icon="notebook-plus"></b-icon>
              <span> Book </span>
            </template>
            <section class="section">
              <div class="columns is-mobile">
                  <b-field class="column" label="From" label-position="on-border">
                    <b-input v-model="form.from"></b-input>
                  </b-field>
                  <b-field class="column" label="To" label-position="on-border">
                    <b-input v-model="form.to"></b-input>
                  </b-field>
                  <br>
                  <b-field class="column" label="Number of passengers" label-position="on-border">
                    <b-input v-model="form.passengers"></b-input>
                  </b-field>
              </div>
              <div class="columns is-mobile">
                <b-field class="column" label="Depart" label-position="on-border">
                  <b-autocomplete
                    v-model="form.depart"
                    :data="filteredDataObj"
                    field="formatted"
                    open-on-focus/>
                </b-field>
<!--                <b-field class="column" label="Return" label-position="on-border">-->
<!--                  <b-autocomplete-->
<!--                    v-model="form.return"-->
<!--                    :data="filteredDataObj"-->
<!--                    field="formatted"-->
<!--                    open-on-focus/>-->
<!--                </b-field>-->
                <div class="column">
                  <b-button class="is-primary" @click="toSearch" icon-left="magnify">Search</b-button>
                </div>
              </div>
            </section>
          </b-tab-item>
          <b-tab-item>
            <template #header>
              <b-icon icon="information-outline"></b-icon>
              <span> Flight Status </span>
            </template>
            <section class="section">
              <SearchRoute/>
            </section>
          </b-tab-item>
        </b-tabs>
      </section>
    </div>
  </section>
</template>

<script>
import SearchRoute from '~/components/SearchRoute'

export default {
  name: 'HomePage',
  components: {
    SearchRoute
  },
  created() {
    this.loadSupportedDates(2)
  },
  data() {
    return {
      supportedDates: [],
      form: {
        from: '',
        to: '',
        passengers: '',
        depart: '',
        return: '',
        selectedDepart: '',
        selectedReturn: ''
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
    // async toSearch() {
    async toSearch() {
      this.form.selectedDepart = this.supportedDates.find(obj => {
        return obj.formatted === this.form.depart
      })
      // this.form.selectedReturn = this.supportedDates.find(obj => {
      //   return obj.formatted === this.form.return
      // })
      console.log(this.form)
      const flights = await this.$axios.$get("http://localhost:8080/api/v1/routes/available", {
        params: {
          to: this.form.to,
          from: this.form.from,
          // passengers: this.form.passengers,
          depart: this.form.selectedDepart.date,
          // return: this.form.selectedDepart.return
        }
      })

      console.log(flights)
      await this.$store.dispatch('flightsFounded', flights)
      await this.$router.push({
        path: '/routes/available',
      })
    }
  }
}
</script>
