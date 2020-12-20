import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import AuthService from '../service/AuthService';
import GenService from '../service/GenService';
import Message from './Message';

class Login extends Component {

    constructor(props) {
        super(props)
        this.state = {
            user: '',
            password: '',
            msgObj: {
              msg: '',
              isError: true
            }
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.loginCallback = this.loginCallback.bind(this);
    }

    componentDidMount(){
        let params = GenService.getParams(this.props.location.search);
        if(params.logout){
            this.updateMessage('Logged out');
        }
    }

    handleChange(event) {
        this.setState(
            {
                [event.target.name] : event.target.value
            }
        )
    }

    handleSubmit(event) {
        event.preventDefault();
        AuthService.login(this.state.user, this.state.password, this.loginCallback);
    }

    loginCallback(loggedIn, err='') {
        if(loggedIn) {
            this.updateMessage('');
            this.props.updateAppUser(this.state.user.toUpperCase());
            this.props.history.push(`/Job`);
        }
        else {
            // let errStr = JSON.stringify(err);
            // let msg = err.message;

            // if(errStr.indexOf('Network') === -1){
            //     msg = 'Invalid credentials';
            // }

            this.updateMessage(err, true);
        }
    }

    updateMessage(msg, isError = false){
        this.setState({msgObj: {msg:msg, isError:isError}});
    }

    render() {
        return (
            <div className="form-region">
                <Message msgObj={this.state.msgObj} />
                <table cellPadding="0" border="0" cellSpacing="0" summary="" className="form-standard">
                    <tbody>
                        <tr>
                            <th><div>JTrack - Login</div></th>
                        </tr>
                        <tr>
                            <td>
                                <form onSubmit={this.handleSubmit}>
                                    <table>
                                        <tbody>
                                            <tr>
                                                <td>User:</td>
                                                <td><input type="text" name="user" size="30" maxLength="40" value={this.state.user} onChange={this.handleChange} /></td>
                                            </tr>
                                            <tr>
                                                <td>Password:</td>
                                                <td><input type="password" name="password" size="30" maxLength="32" value={this.state.password} onChange={this.handleChange} /></td>
                                            </tr>
                                            <tr>
                                                <td></td>
                                                <td><input type="submit" value="Login" /></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </form>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        )
    }
}

export default withRouter(Login);