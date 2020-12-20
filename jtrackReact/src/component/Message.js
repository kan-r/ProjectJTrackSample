import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import AuthService from '../service/AuthService';

class Message extends Component {

    logout(){
        AuthService.logout();
        this.props.history.push("/Login");
    }

    renderMsg(){
        let msg = this.props.msgObj.msg;
       
        if(msg === null || msg === ''){
            return '';
        }

        if(typeof(msg) === 'object'){
            if(msg.response !== undefined){
                msg = msg.response.data;
                if(typeof(msg) === 'object'){
                    if(msg.error !== undefined){
                        // msg = msg.error;
                        // if(msg === 'invalid_token'){
                        //     msg = 'Session expired';
                        //     this.logout();  
                        // }else if(msg === 'invalid_grant'){
                        //     msg = 'Bad credentials';    
                        // }

                        if(msg.error === 'invalid_token'){
                            msg = 'Session expired';
                            this.logout();  
                        }else if(msg.error === 'invalid_grant'){
                            msg = 'Bad credentials';    
                        }else if(msg.error == 'invalid_data'){
                            msg = msg.message
                        }else{
                            msg = msg.error;
                        }
                    }
                }
            }else {
                msg = msg.message;
            }
        }

        if(msg === null || msg === ''){
            return '';
        }

        if(this.props.msgObj.isError){
            return(<div className="alert alert-danger">{msg}</div>);
        }else{
            return(<div className="alert alert-info">{msg}</div>);
        }
    }

    render(){
        return (
            <div className="container">
                {this.renderMsg()}
            </div>
        );
    }
}

export default withRouter(Message);