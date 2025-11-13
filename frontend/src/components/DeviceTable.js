import React from "react";
import Table from "../tables/table";
import { getColumns } from "../tables/tableLibrary";

export default function DeviceTable({ devices }) {
    const columns = getColumns([
        { header: "Device Name", accessor: "name" },
        { header: "Maximum Consumption (W)", accessor: "maximumConsumption" },
        { header: "Year of Manufacture", accessor: "yearOfManufacture" },
    ]);

    const search = [
        { accessor: "name" },
        { accessor: "maximumConsumption" },
        { accessor: "yearOfManufacture" },
    ];

    return (
        <div style={{ marginTop: "10px" }}>
            <Table data={devices} columns={columns} search={search} pageSize={5} />
        </div>
    );
}
