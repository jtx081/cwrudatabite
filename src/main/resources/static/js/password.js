// class for password validation

class Password {
  constructor(str) {
    this.password = str
  }
  match(p2) {
    return this.password == p2.password
  }
  lessEq(n) {
    return this.password.length <= n
  }
  greaterEq(n) {
    return this.password.length >= n
  }
  matchStr(str) {
    return this.password == str
  }
  isEmpty() {
    return str == ""
  }
}
