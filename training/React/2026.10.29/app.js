//html 사용하는 것이 아니고 다른 js 파일에서 이 js파일을 사용하고 싶다
//app.js  >  data.js에서 사용
//script 사용
import {pi, sum} from './data.js'

console.log(pi);
const data = sum(10, 20);
console.log("data: " + data);