import Config from '../settings'

const tokenKey = Config.tokenKey

function getToken(){
    return sessionStorage.getItem(tokenKey)
}

function setToken(token){
    return sessionStorage.setItem(tokenKey, token)
}
function removeToken(){
    return sessionStorage.removeItem(tokenKey)
}

export default {
    getToken,
    setToken,
    removeToken
}