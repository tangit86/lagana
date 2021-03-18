import React from 'react';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import { Container, Row, Col } from 'react-bootstrap'

export class FormView extends React.Component {

    constructor(props) {
        super(props)

        this.state = {
            isEdit: false
        }

        this.onChange = this.onChange.bind(this)
    }

    onChange(id, key, e) {
        this.props.onChange(id, key, e.target.value);
    }

    render() {
        const row = this.props.row;
        const cols = this.props.cols;
        const onSave = this.props.onSave;
        return (
            <Container fluid="md" style={{ width: "100%" }}>
                <Row>
                    <Col ></Col>
                    <Col ></Col>
                    <Col ></Col>
                    <Col style={{ align: 'right' }}>
                        <label class="switch">
                            <input type="checkbox" value={this.state.isEdit} onChange={() => { this.setState({ isEdit: !this.state.isEdit }) }} />
                            <span class="slider round"></span>
                        </label>
                        <span>{this.state.isEdit ? "Edit mode" : "View mode"}</span>
                    </Col>
                </Row>
                {
                    cols.map(col => {
                        const cell = row[col.id];
                        return [<Row fluid="md">
                            {
                                this.state.isEdit ?
                                    getFormValuesWrapper(col.id, col.editor ? col.editor(cell) : getStandardCellEditor(cell, (e) => this.onChange(row.name, col.id, e)))
                                    :
                                    getFormValuesWrapper(col.id, col.formatter ? col.formatter(cell) : getStandardCellFormat(cell))
                            }
                        </Row>]
                    })
                }
            </Container>
        )
    }
}


function getStandardCellFormat(cell) {
    if (typeof (cell) === "boolean") {
        return getBooleanCellFormat(cell)
    }
    return cell;
}


function getStandardCellEditor(cell, onChange) {
    console.log("typeof cell: ", typeof (cell))
    if (typeof (cell) === "boolean") {
        return getBooleanCellEditor(cell, onChange)
    }
    return (<input width="box-sizing: border-box;" type="text" value={cell} onChange={(e) => onChange(e)} />);
}


function getBooleanCellFormat(cell) {
    return cell ? 'YES' : 'NO';
}


function getBooleanCellEditor(cell, onChange) {
    return (<div>
        <label class="switch">
            <input type="checkbox" value={cell} onChange={onChange} />
            <span class="slider round"></span>
        </label>
    </div>)
}


function getFormValuesWrapper(label, content) {
    return (<fieldset class="scheduler-border" width="box-sizing: border-box;%">
        <legend class="scheduler-border">{label}</legend>
        <div class="control-group">
            <div class="controls bootstrap-timepicker">
                {content}
            </div>
        </div>
    </fieldset>)
}