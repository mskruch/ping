import React, {Component} from "react";
import moment from "moment";

const Outages = (props) => {
    return (
        <small>
            <table className="table table-sm outages">
                <tbody>
                {props.outages.map((outage, i) =>
                    <tr key={i} className={outage.finished ? '' : 'down'}>
                        <td width="50%">
                            <span data-toggle="tooltip" data-placement="right"
                                  title="outage started">
                                {moment(outage.started).format('YYYY-MM-DD HH:mm')}
                            </span>
                        </td>
                        <td width="30%">
                            <span data-toggle="tooltip" data-placement="right"
                                  title="downtime">
                                {outage.finished ? moment(outage.finished).from(outage.started, true) : 'still down'}
                            </span>
                        </td>
                        <td width="20%">
                            {outage.notified &&
                            <span data-toggle="tooltip" data-placement="right"
                                  title="notification has been sent">
                                notified
                            </span>}
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </small>
    );
}

export default Outages;