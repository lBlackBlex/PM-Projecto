import Vue from 'vue'

const months = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December"
]

const weekdays = [
  "Sunday",
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday",
  "Saturday"
]

const dateFilter = inputDate => {
  const date = new Date(inputDate)
  const year = date.getFullYear()
  const month = date.getMonth()
  const day = date.getDate()
  return weekdays[date.getDay()] + ", " + months[month] + " " + day + ", " + year
}

const timeFilter = inputDate => {
  const date = new Date(inputDate)
  const hours = date.getHours()
  const minutes = date.getMinutes()
  const minutesFormatted = minutes < 10 ? '0' + minutes : minutes;
  const ampm = hours >= 12 ? 'pm' : 'am'
  return hours + ":" + minutesFormatted + " " + ampm.toUpperCase()
}

const dateTimeFilter = inputDate => {
  const date = new Date(inputDate)
  const year = date.getFullYear()
  const month = date.getMonth()
  const monthFormatted = month < 10 ? '0' + (month + 1) : (month + 1)
  const day = date.getDate()
  const hours = date.getHours()
  const hoursFormatted = hours < 10 ? '0' + hours : hours;
  const minutes = date.getMinutes()
  const minutesFormatted = minutes < 10 ? '0' + minutes : minutes;
  return year + '-' + monthFormatted + '-' + day + ' ' + hoursFormatted + ":" + minutesFormatted
}

Vue.filter('date', dateFilter)
Vue.filter('time', timeFilter)
Vue.filter('datetime', dateTimeFilter)
