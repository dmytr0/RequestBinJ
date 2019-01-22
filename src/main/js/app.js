'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
var i = 0;


class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {listrequests: []};
    }

    componentWillUnmount() {
        clearInterval(this.interval);
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/listrequests'}).done(response => {
            i = response.entity.length;
            this.setState({listrequests: response.entity});
        });
        this.interval = setInterval(() => client({method: 'GET', path: '/api/listrequests'}).done(response => {
            i = response.entity.length;
            this.setState({listrequests: response.entity});
        }), 1000);
    }

    render() {
        return (
            <MyRequestEntityList listrequests={this.state.listrequests}/>
        )
    }
}

class MyRequestEntityList extends React.Component {
    render() {
        var listrequests = this.props.listrequests.map(request =>
            <MyRequestEntity key={request.id} request={request}/>
        );
        return (
            <div>
                <div className="counter"><a>Requests count: {i}</a></div>
                <div>
                    {listrequests}
                </div>
            </div>
        )
    }
}


class MyRequestEntity extends React.Component {
    render() {

        let formattedBody = getPrettyBody(this.props.request.body, this.props.request.headers);


        return (
            <div className="request">
                <table className="request-table">
                    <tbody>
                    <tr>
                        <td className="method">{this.props.request.method}</td>
                        <td className="request_id">{this.props.request.id}</td>
                    </tr>
                    <tr>
                        <td className="name time_name">Time</td>
                        <td className="request_time">{this.props.request.stringTime}</td>
                    </tr>
                    <tr>
                        <td className="name body_name ">Body</td>
                        <td className="body_value">
                            <pre className="body-area">{formattedBody}</pre>
                        </td>
                    </tr>
                    <tr>
                        <td className="name header_name">Headers</td>
                        <td className="header_values"><Headers clazz="headers" headers={this.props.request.headers}/>
                        </td>
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

function getPrettyBody(body, headers) {
    let formattedBody = body;
    if (headers['content-type'] === 'application/json') {
        try {
            formattedBody = JSON.stringify(JSON.parse(formattedBody), null, 2);
        } catch (ignore) {
        }
    }

    if (headers['content-type'] === 'application/xml' || headers['content-type'] === 'text/xml') {
        try {
            formattedBody = formatXml(formattedBody);
        } catch (ignore) {
        }
    }

    return formattedBody;
}

function formatXml(xml) {
    var formatted = '';
    var reg = /(>)(<)(\/*)/g;
    xml = xml.replace(reg, '$1\r\n$2$3');
    var pad = 0;
    jQuery.each(xml.split('\r\n'), function (index, node) {
        var indent = 0;
        if (node.match(/.+<\/\w[^>]*>$/)) {
            indent = 0;
        } else if (node.match(/^<\/\w/)) {
            if (pad != 0) {
                pad -= 1;
            }
        } else if (node.match(/^<\w[^>]*[^\/]>.*$/)) {
            indent = 1;
        } else {
            indent = 0;
        }

        var padding = '';
        for (var i = 0; i < pad; i++) {
            padding += ' ';
        }

        formatted += padding + node + '\r\n';
        pad += indent;
    });

    return formatted;
}


class Headers extends React.Component {
    render() {
        var clazz = this.props.clazz;
        var headers = Object.entries(this.props.headers).map(([hname, value]) =>
            <Header key={hname} clazz={clazz} hname={hname} value={value}/>
        );

        return (
            <table className="headers">
                <tbody>
                {headers}
                </tbody>
            </table>
        )
    }
}

class Header extends React.Component {
    render() {
        var clazz = this.props.clazz;
        return (
            <tr>
                <td><span className={"hname " + clazz}>{this.props.hname}</span></td>
                <td><span className="hvalue">{this.props.value}</span></td>
            </tr>
        )
    }
}


ReactDOM.render(
    <App/>,
    document.getElementById('react')
);



