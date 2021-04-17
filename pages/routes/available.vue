<template>
  <section class="section">
    <div class="columns is-mobile">
      <section class="hero is-small is-info column">
        <div class="hero-body">
          <p class="title">
            Depart {{ flightData.from }} to {{ flightData.to }}
          </p>
          <p class="subtitle">
            {{ formatDate | date }}
          </p>
        </div>
      </section>
    </div>
    <br>
      <div
        class="columns is-mobile" v-for="item in flightsFounded">
        <card
          :flight="item"
        >
        </card>
      </div>
  </section>
</template>

<script>
import Card from "~/components/Card";

export default {
  components: [
    Card
  ],
  computed: {
    formatDate(){
      const fd = this.flightData.date.split('-')
      return fd[0] + ' ' + fd[1] + ' ' + fd[2]
    }
  },
  created() {
    // console.log(this.flightsFounded)
    this.flightsFounded = this.$store.getters.flightsFounded
    this.flightData = this.flightsFounded[0]
    console.log(this.flightsFounded)
  },
  data() {
    return {
      flightsFounded: [],
      flightData: {}
    }
  }
}
</script>
