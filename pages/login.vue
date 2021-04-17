<template>
  <section class="section">
    <div class="columns is-mobile">
      <div class="block">
        <b-field label="Email" label-position="on-border">
          <b-input type="email" v-model="form.email" maxlength="30"/>
        </b-field>
        <b-field label="Password" label-position="on-border">
          <b-input type="password" password-reveal v-model="form.password"  maxlength="30"/>
        </b-field>
        <b-button @click="login" label="Login" type="is-primary"></b-button>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  data(){
    return {
      form: {
        email: '',
        password: '',
      }
    }
  },
  methods: {
    login(){
      const credentials = JSON.stringify({
        username: this.form.email,
        password: this.form.password
      })
      this.$store.dispatch('loginUser', credentials)
    }
  }
}
</script>
