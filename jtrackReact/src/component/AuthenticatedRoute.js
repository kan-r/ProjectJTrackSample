import React, { Component } from 'react'
import { Route, Redirect } from 'react-router-dom'
import AuthService from '../service/AuthService';

class AuthenticatedRoute extends Component {
    render() {
        if (AuthService.isUserLoggedIn()) {

            if(!AuthService.isUserAdmin()){
                if(this.props.path === "/UserCreate"){
                    return <Redirect to="/User" />
                }
                if(this.props.path === "/UserEdit/:userId"){
                    return <Redirect to="/User" />
                }
              }

            return <Route {...this.props} />
        } else {
            return <Redirect to="/Login" />
        }
    }
}

export default AuthenticatedRoute