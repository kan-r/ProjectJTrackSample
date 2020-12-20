import axios from 'axios';

const DEBUG = false;

const BASE_URL = 'http://localhost:8082';
// const BASE_URL = 'https://kan-r.com/jtrackREST';

const USER_SESSION_ATTRIBUTE = 'USER';
const TOKEN_SESSION_ATTRIBUTE = 'TOKEN';

class AuthService {

    login(user, password, loginCallback){
        if(user == null || user === '' || password == null || password === ''){
            loginCallback(false);
            return;
        }

        const options = {
           mode: 'cores',
           headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': 'Basic ' + window.btoa("jtrackAdmin:admin")
           },
           params: {
             username: user,
             password: password,
             grant_type:'password'
           } 
        }

        axios.post(BASE_URL + '/oauth/token', {}, options)
        .then(res => {
            this.addToSessionStorage(user, res.data);
            this.log("Logged in");
            loginCallback(true);
        })
        .catch(err => {
            this.logError(err);
            loginCallback(false, err);
        });
    }

    isUserLoggedIn() {
        let token = this.getAccessToken();
        return (token !== null && token !== '');
    }

    isUserAdmin(){
        let user = this.getAppUser();
        return (user === 'ADMIN');
    }

    logout(){
        this.removeFromSessionStorage();
        this.log("Logged out");
    }

    getAccessToken(){
        // return sessionStorage.getItem(TOKEN_SESSION_ATTRIBUTE);
        let sessObj = sessionStorage.getItem(TOKEN_SESSION_ATTRIBUTE);
        if(sessObj !== null){
            let sessJSON = JSON.parse(sessObj);

            // if(new Date().getTime() > sessJSON.expires_at){
            //     this.logError("Access token expired");
            //     this.logout();
            //     return;
            // }

            return sessJSON.access_token;
        }
        return null;
    }

    getAppUser(){
        return sessionStorage.getItem(USER_SESSION_ATTRIBUTE);
    }

    getBaseUrl(){
        return BASE_URL;
    }

    getHttpOptions(){
        const options = {
            mode: 'cors',
            headers: {
             'Content-Type': 'application/json',
             'Authorization': 'Bearer ' + this.getAccessToken()
            }
         }

         return options;
    }

    addToSessionStorage(user, token){
        
        let  expireDate = new Date().getTime() + (1000 * token.expires_in);

        let sessObj = {
            'access_token': token.access_token, 
            'token_type': token.token_type,
            'expires_at': expireDate,
            'scope': token.scope
        };
       
        sessionStorage.setItem(USER_SESSION_ATTRIBUTE, user.toUpperCase());
        sessionStorage.setItem(TOKEN_SESSION_ATTRIBUTE, JSON.stringify(sessObj));
    }

    removeFromSessionStorage(){
        sessionStorage.removeItem(USER_SESSION_ATTRIBUTE);
        sessionStorage.removeItem(TOKEN_SESSION_ATTRIBUTE);
    }

    log(message){
        if(DEBUG){
            console.log(message);
        }
    }

    logError(error){
        if(DEBUG){
            console.error(error);
        }
    }
}

export default new AuthService()