<template>
  <section>
    <b-field
      label="Type"
      label-position="on-border">
      <b-input :value="formatType(terminal.type)" disabled/>
    </b-field>
  </section>
</template>

<script>
export default {
  props: {
    terminal: {
      type: Object,
      required: true
    },
  },
  methods: {
    goBack() {
      this.$router.back()
    },
    formatType(type) {
      if (type === 1)
        return 'National'
      else
        return 'International'
    },
  },
}
</script>
