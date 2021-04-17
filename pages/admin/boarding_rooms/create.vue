<template>
  <section class="section">
    <div class="columns is-mobile">
      <BoardingRoomForm
        class="column"
        :boardingRoom="{}"
        :showButton="true"/>
    </div>
  </section>
</template>

<script>

import BoardingRoomForm from "~/components/BoardingRoomForm";

export default {
  components: [
    BoardingRoomForm
  ],
  middleware: ['check-auth', 'auth'],
}
</script>
