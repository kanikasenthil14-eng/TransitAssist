/* =========================================
   TransitAssist - Frontend JavaScript
========================================= */


/* =========================================
   Create Assistance Request
========================================= */

const requestForm = document.getElementById("requestForm");

if (requestForm) {

    requestForm.addEventListener("submit", function (event) {

        event.preventDefault();

        const userId = document.getElementById("userId").value.trim();
        const pickupPoint = document.getElementById("pickupPoint").value.trim();
        const destination = document.getElementById("destination").value.trim();
        const assistanceType = document.getElementById("assistanceType").value;
        const requestDate = document.getElementById("requestDate").value;
        const startTime = document.getElementById("startTime").value;
        const endTime = document.getElementById("endTime").value;

        const requestData = {
            userId: userId,
            pickupPoint: pickupPoint,
            destination: destination,
            assistanceType: assistanceType,
            requestDate: requestDate,
            startTime: startTime,
            endTime: endTime
        };

        fetch("/requests", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(requestData)

        })

        .then(response => {

            if (!response.ok) {
                throw new Error("Failed to create request");
            }

            return response.json();

        })

        .then(data => {

            alert("Assistance request created successfully!");

            requestForm.reset();

            window.location.href = "/requests.html";

        })

        .catch(error => {

            console.error("Error:", error);

            alert("Failed to create assistance request.");

        });

    });

}


/* =========================================
   Load Assistance Requests
========================================= */

const requestTableBody = document.getElementById("requestTableBody");

if (requestTableBody) {

    fetch("/requests")

        .then(response => {

            if (!response.ok) {
                throw new Error("Failed to load requests");
            }

            return response.json();

        })

        .then(requests => {

            requestTableBody.innerHTML = "";

            if (requests.length === 0) {

                requestTableBody.innerHTML = `
                    <tr>
                        <td colspan="11">
                            No assistance requests found.
                        </td>
                    </tr>
                `;

                return;
            }

            requests.forEach(request => {

                const row = document.createElement("tr");
                const statusClass = {
                    REQUESTED: "status-requested",
                    ASSIGNED: "status-assigned",
                    COMPLETED: "status-completed",
                    CANCELLED: "status-cancelled"
                }[request.status] || "";
                const actionsDisabled = ["COMPLETED", "CANCELLED"].includes(request.status)
                    ? "disabled"
                    : "";

                row.innerHTML = `

                    <td>
                        ${request.requestid || "-"}
                    </td>

                    <td>
                        ${request.user?.name || "-"}
                    </td>

                    <td>
                        ${request.pickupPoint || "-"}
                    </td>

                    <td>
                        ${request.destination || "-"}
                    </td>

                    <td>
                        ${request.tripDate || "-"}
                    </td>

                    <td>
                        ${request.pickupTime || "-"}
                    </td>

                    <td>
                        ${request.endTime || "-"}
                    </td>

                    <td>
                        ${request.assistanceType || "-"}
                    </td>

                    <td>
                        ${request.helper?.name || "-"}
                    </td>

                    <td>
                        <span class="status ${statusClass}">
                            ${request.status || "-"}
                        </span>
                    </td>

                    <td>

                        <button
                            class="button secondary-button"
                            onclick="assignHelper(${request.requestid})"
                            ${actionsDisabled}>
                            Assign
                        </button>

                        <button
                            class="button secondary-button"
                            onclick="cancelRequest(${request.requestid})"
                            ${actionsDisabled}>
                            Cancel
                        </button>

                        <button
                            class="button primary-button"
                            onclick="completeRequest(${request.requestid})"
                            ${actionsDisabled}>
                            Complete
                        </button>

                    </td>

                `;

                requestTableBody.appendChild(row);

            });

        })

        .catch(error => {

            console.error("Error:", error);

            requestTableBody.innerHTML = `
                <tr>
                    <td colspan="11">
                        Failed to load assistance requests.
                    </td>
                </tr>
            `;

        });

}


/* =========================================
   Assign Helper
========================================= */

function assignHelper(requestId) {

    const helperId = prompt("Enter Helper ID:");

    if (!helperId) {
        return;
    }

    fetch(`/requests/${requestId}/assign/${helperId}`, {

        method: "PUT"

    })

    .then(response => {

        if (!response.ok) {
            throw new Error("Failed to assign helper");
        }

        return response.json();

    })

    .then(data => {

        alert("Helper assigned successfully!");

        location.reload();

    })

    .catch(error => {

        console.error("Error:", error);

        alert(
            "Could not assign a helper. Check the Helper ID, availability, " +
            "and request status."
        );

    });

}


/* =========================================
   Cancel Request
========================================= */

function cancelRequest(requestId) {

    const confirmCancel = confirm(
        "Are you sure you want to cancel this request?"
    );

    if (!confirmCancel) {
        return;
    }

    fetch(`/requests/${requestId}/cancel`, {

        method: "PUT"

    })

    .then(response => {

        if (!response.ok) {
            throw new Error("Failed to cancel request");
        }

        return response.json();

    })

    .then(data => {

        alert("Request cancelled successfully!");

        location.reload();

    })

    .catch(error => {

        console.error("Error:", error);

        alert(
            "Only active requests can be cancelled. " +
            "Completed and cancelled requests are final."
        );

    });

}


/* =========================================
   Complete Request
========================================= */

function completeRequest(requestId) {

    const confirmComplete = confirm(
        "Are you sure you want to mark this request as completed?"
    );

    if (!confirmComplete) {
        return;
    }

    fetch(`/requests/${requestId}/complete`, {

        method: "PUT"

    })

    .then(response => {

        if (!response.ok) {
            throw new Error("Failed to complete request");
        }

        return response.json();

    })

    .then(data => {

        alert("Request completed successfully!");

        location.reload();

    })

    .catch(error => {

        console.error("Error:", error);

        alert(
            "Only active requests can be completed. " +
            "Completed and cancelled requests are final."
        );

    });

}


/* =========================================
   LOAD HELPERS
========================================= */

const helperTableBody = document.getElementById("helperTableBody");


function loadHelpers() {

    if (!helperTableBody) {
        return;
    }

    fetch("/helpers")

        .then(response => {

            if (!response.ok) {
                throw new Error("Failed to load helpers");
            }

            return response.json();

        })

        .then(helpers => {

            helperTableBody.innerHTML = "";

            if (helpers.length === 0) {

                helperTableBody.innerHTML = `
                    <tr>
                        <td colspan="7">
                            No helpers available.
                        </td>
                    </tr>
                `;

                return;
            }

            helpers.forEach(helper => {

                const row = document.createElement("tr");

                const availability =
                    helper.available
                        ? "Available"
                        : "Not Available";

                row.innerHTML = `

                    <td>
                        ${helper.helperId || "-"}
                    </td>

                    <td>
                        ${helper.name || "-"}
                    </td>

                    <td>
                        ${helper.email || "-"}
                    </td>

                    <td>
                        ${helper.phone || "-"}
                    </td>

                    <td>
                        ${helper.assistanceType || "-"}
                    </td>

                    <td>
                        ${availability}
                    </td>

                    <td>

                        <a
                            href="/workload.html?helperId=${helper.helperId}"
                            class="button secondary-button">

                            Workload

                        </a>

                    </td>

                `;

                helperTableBody.appendChild(row);

            });

        })

        .catch(error => {

            console.error("Error:", error);

            helperTableBody.innerHTML = `
                <tr>
                    <td colspan="7">
                        Failed to load helpers.
                    </td>
                </tr>
            `;

        });

}


/* Load helpers when Helpers page opens */

if (helperTableBody) {
    loadHelpers();
}


/* =========================================
   ADD NEW HELPER
========================================= */

const showAddHelperButton =
    document.getElementById("showAddHelperButton");

const addHelperFormContainer =
    document.getElementById("addHelperFormContainer");

const addHelperForm =
    document.getElementById("addHelperForm");

const cancelAddHelperButton =
    document.getElementById("cancelAddHelperButton");

const helperFormMessage =
    document.getElementById("helperFormMessage");


/* -----------------------------------------
   Show Add Helper Form
----------------------------------------- */

if (showAddHelperButton) {

    showAddHelperButton.addEventListener("click", function () {

        addHelperFormContainer.style.display = "block";

        showAddHelperButton.style.display = "none";

        if (helperFormMessage) {
            helperFormMessage.textContent = "";
        }

    });

}


/* -----------------------------------------
   Cancel Add Helper
----------------------------------------- */

if (cancelAddHelperButton) {

    cancelAddHelperButton.addEventListener("click", function () {

        addHelperForm.reset();

        addHelperFormContainer.style.display = "none";

        showAddHelperButton.style.display = "inline-block";

        if (helperFormMessage) {
            helperFormMessage.textContent = "";
        }

    });

}


/* -----------------------------------------
   Submit Add Helper Form
----------------------------------------- */

if (addHelperForm) {

    addHelperForm.addEventListener("submit", function (event) {

        event.preventDefault();


        /* Get form values */

        const name =
            document.getElementById("helperName").value.trim();

        const email =
            document.getElementById("helperEmail").value.trim();

        const phone =
            document.getElementById("helperPhone").value.trim();

        const assistanceType =
            document.getElementById("helperAssistanceType").value;

        const availableValue =
            document.getElementById("helperAvailable").value;


        /* Convert string to boolean */

        const available =
            availableValue === "true";


        /* Create helper object */

        const helperData = {

            name: name,

            email: email,

            phone: phone,

            assistanceType: assistanceType,

            available: available

        };


        /* Show saving message */

        if (helperFormMessage) {

            helperFormMessage.textContent =
                "Adding helper...";

        }


        /* Send data to Spring Boot */

        fetch("/helpers", {

            method: "POST",

            headers: {

                "Content-Type": "application/json"

            },

            body: JSON.stringify(helperData)

        })

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Failed to add helper"
                );

            }

            return response.json();

        })

        .then(data => {

            if (helperFormMessage) {

                helperFormMessage.textContent =
                    "Helper added successfully!";

            }


            /* Clear form */

            addHelperForm.reset();


            /*
             * Hide form after successful addition
             */

            setTimeout(function () {

                addHelperFormContainer.style.display =
                    "none";

                showAddHelperButton.style.display =
                    "inline-block";

                helperFormMessage.textContent = "";

                /* Reload helper list */

                loadHelpers();

            }, 500);

        })

        .catch(error => {

            console.error("Error:", error);

            if (helperFormMessage) {

                helperFormMessage.textContent =
                    "Failed to add helper. Please try again.";

            }

        });

    });

}


/* =========================================
   HELPER WORKLOAD
========================================= */

const workloadContainer =
    document.getElementById("workloadContainer");

if (workloadContainer) {

    const urlParams =
        new URLSearchParams(window.location.search);

    const helperId =
        urlParams.get("helperId");

    const workloadDate =
        document.getElementById("workloadDate");


    function loadWorkload() {

        if (!helperId) {

            workloadContainer.innerHTML = `
                <p>
                    Helper ID not provided.
                </p>
            `;

            return;

        }

        const date =
            workloadDate ? workloadDate.value : "";


        let url =
            `/helpers/${helperId}/workload`;

        if (date) {

            url += `?date=${date}`;

        }


        fetch(url)

            .then(response => {

                if (!response.ok) {

                    throw new Error(
                        "Failed to load workload"
                    );

                }

                return response.json();

            })

            .then(data => {

                workloadContainer.innerHTML = `

                    <h2>
                        Helper Workload
                    </h2>

                    <p>
                        Helper ID:
                        <strong>${helperId}</strong>
                    </p>

                    <p>
                        Total Assigned Requests:
                        <strong>
                            ${data.totalRequests || 0}
                        </strong>
                    </p>

                `;

            })

            .catch(error => {

                console.error("Error:", error);

                workloadContainer.innerHTML = `
                    <p>
                        Failed to load helper workload.
                    </p>
                `;

            });

    }


    loadWorkload();


    if (workloadDate) {

        workloadDate.addEventListener(
            "change",
            loadWorkload
        );

    }

}