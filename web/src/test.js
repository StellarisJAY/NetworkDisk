import axios from 'axios'

let success = 0
let fail = 0
for(let i = 1; i <= 100000; i++){
    axios.get("http://localhost:8080/reservations/SearchMenu")
    .then(response=>{
        if(response.status == 200){
            success += 1
        }
        else{
            fail += 1
        }
    })
    .catch(error=>{
        fail += 1
    })
}