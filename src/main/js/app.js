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
            prettyPrintStart();
        });
        this.interval = setInterval(() => client({method: 'GET', path: '/api/listrequests'}).done(response => {
            i = response.entity.length;
            this.setState({listrequests: response.entity});
            prettyPrintStart();
        }), 1000);
    }

    render() {
        return (
            <MyRequestEntityList listrequests={this.state.listrequests}/>
        )
    }
}

function prettyPrintStart() {
    try {
        PR.prettyPrint();
    } catch (e) {
        console.log("pretty print error ");
        console.log(e);
    }
}

class MyRequestEntityList extends React.Component {
    render() {
        var listrequests = this.props.listrequests.map(request =>
            <MyRequestEntity key={request._id} request={request}/>
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
                        <td className="request_id">{this.props.request._id}</td>
                    </tr>
                    <tr>
                        <td className="name time_name">Time</td>
                        <td className="request_time">{this.props.request.stringTime}</td>
                    </tr>
                    <tr>
                        <td className="name body_name ">Body</td>
                        <td className="body_value">{formattedBody}</td>
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
            </div>
        )
    }
}

function getPrettyBody(body, headers) {
    let formattedBody = body;
    if (headers['content-type'] !== undefined) {
        let contType = headers['content-type'];
        if (contType.indexOf('application/json') !== -1) {
            try {
                formattedBody = formatJson(formattedBody);
                let innerHtml = {__html: formattedBody};
                return <pre className="wrap-class" dangerouslySetInnerHTML={innerHtml}/>;
            } catch (ignore) {
            }
        } else {
            return <pre className="prettyprint wrap-class">{formattedBody}</pre>;
        }

        // if (contType.indexOf('application/xml') !== -1 || contType.indexOf('text/xml') !== -1) {
        //     try {
        //         formattedBody = formatXml(formattedBody);
        //         return <pre className="prettyprint wrap-class">{formattedBody}</pre>;
        //     } catch (ignore) {
        //     }
        // }

    } else {
        return <pre className="prettyprint wrap-class">{formattedBody}</pre>;
    }
}

function formatJson(json) {
    let jObj = JSON.parse(json);
    json = JSON.stringify(jObj, undefined, 3);
    json = syntaxHighlight(json);
    return json;
}

function syntaxHighlight(json) {
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}

// function formatXml(xml) {
//     let padNumber = 2;
//     var formatted = '';
//     var reg = /(>)(<)(\/*)/g;
//     xml = xml.replace(reg, '$1\r\n$2$3');
//     var pad = 0;
//     xml.split('\r\n').forEach(function (node, index) {
//         var indent = 0;
//         if (node.match(/.+<\/\w[^>]*>$/)) {
//             indent = 0;
//         } else if (node.match(/^<\/\w/)) {
//             if (pad != 0) {
//                 pad -= padNumber;
//             }
//         } else if (node.match(/^<\w[^>]*[^\/]>.*$/)) {
//             indent = padNumber;
//         } else {
//             indent = 0;
//         }
//
//         var padding = '';
//         for (var i = 0; i < pad; i++) {
//             padding += ' ';
//         }
//
//         formatted += padding + node + '\r\n';
//         pad += indent;
//     });
//
//     return formatted;
// }
//

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



