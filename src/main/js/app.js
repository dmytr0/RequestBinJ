'use strict';

// tag::vars[]
const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
// end::vars[]

// tag::app[]
class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {listrequests: []};
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/listrequests'}).done(response => {
            this.setState({listrequests: response.entity});
        });
    }

    render() {
        return (
            <MyRequestEntityList listrequests={this.state.listrequests}/>
        )
    }
}

// end::app[]

// tag::request-list[]
class MyRequestEntityList extends React.Component {
    render() {
        var listrequests = this.props.listrequests.map(request =>
            <MyRequestEntity key={request.id} request={request}/>
        );
        return (
            <div>
                {listrequests}
            </div>
        )
    }
}


class MyRequestEntity extends React.Component {
    render() {
        return (
            <div className="request">
                <table>
                    <tbody>
                    <tr>
                        <td className="method">{this.props.request.method}</td>
                        <td className="request_id">{this.props.request.id}</td>
                    </tr>
                    <tr>
                        <td className="request_time">{this.props.request.stringTime}</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td className="name body_name">Body</td>
                        <td className="body_value">{this.props.request.body}</td>
                    </tr>
                    <tr>
                        <td className="name header_name">Headers</td>
                        <td className="header_values"><Headers  clazz="headers"  headers={this.props.request.headers}/></td>
                    </tr>
                    <tr>
                        <td className="name params_name">Params</td>
                        <td className="params_values"><Headers clazz="params" headers={this.props.request.params}/></td>
                    </tr>
                    </tbody>
                </table>
                <br/>
                <tr/>
            </div>
        )
    }
}

class Headers extends React.Component {
    render() {
        var clazz = this.props.clazz;
        var headers = Object.entries(this.props.headers).map(([hname, value]) =>
            <Header key={hname} clazz={clazz} hname={hname} value={value}/>
        );

        return (
            <ul className="headers">
                {headers}
            </ul>
        )
    }
}

class Header extends React.Component {
    render() {
        var clazz = this.props.clazz;
        return (
            <li><span className={"hname " + clazz}>{this.props.hname}</span>: <span className="hvalue">{this.props.value}</span></li>
        )
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
)
// end::render[]

