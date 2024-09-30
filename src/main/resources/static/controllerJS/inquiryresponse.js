window.addEventListener('load', () => {
    refreshInquiryResponseForm();
    refreshInquiryResponseTable();

    // Get the date input element
    const dateInput = document.getElementById('inqArrivalDate');

    // Get today's date
    const today = new Date();

    // Add 7 days to today's date
    const minDate = new Date(today.setDate(today.getDate() + 5));

    // Format the date as yyyy-mm-dd
    const formattedDate = minDate.toISOString().split('T')[0];

    // Set the min attribute of the date input
    dateInput.setAttribute('min', formattedDate);
})

//TO REFRESH FORM
const refreshInquiryResponseForm = () => {
    inqResponse = new Object();

    pkgList = ajaxGetRequest('/tourpackagecustom/alldata')
    fillDataIntoSelect(customizedPackage, 'Select Customized Package', pkgList, 'packagename');

    nationalityList = ajaxGetRequest('/nationality/alldata')
    fillDataIntoDataList(dataListNationality, nationalityList, 'countryname');

    nationalityList = ajaxGetRequest('/nationality/alldata')
    fillDataIntoDataList(dataListNationality, nationalityList, 'countryname');
}

const changeLable = () => {
    if (InqClientNationality.value == "Sri Lanka") {
        lblForNICorPpt.innerText = "NIC: "
    } else {
        lblForNICorPpt.innerText = "Passport: "
    }
}

//TO REFRESH TABLE
const refreshInquiryResponseTable = () => {

    //only show inquiries that new or in progress
    inprogressInqs = ajaxGetRequest('/inquiry/newandinprogressonly')

    const displayProperty = [
        { dataType: 'text', propertyName: 'inqcode' },
        { dataType: 'text', propertyName: 'recievedmethod' },
        { dataType: 'text', propertyName: 'inqtype' },
        { dataType: 'text', propertyName: 'clientname' },
        { dataType: 'function', propertyName: getInquiryStatus }

    ]

    fillDataIntoTable3(inqResponseTable, inprogressInqs, displayProperty, buttonVisibility = true) //disableButtonsCommonFn , loggedUserPrivileges 
}

const getInquiryStatus = (ob) => {
    return ob.inquirystatus.name;
}

//show inq type
const ifInqTypeIsGeneral = () => {
    if (inquiry.inqtype == "General") {
        inqInterestedPkg.disabled = true;
        inquiry.based_tpkg_id = null;
    } else {
        inqInterestedPkg.disabled = false;
        inqInterestedPkg.style.border = "1px solid #ced4da";
    }
}

const refillInquiryResponseForm = (ob) => {

    inquiry = JSON.parse(JSON.stringify(ob));
    oldInquiry = JSON.parse(JSON.stringify(ob));

    newResponce = new Object();
    oldResponce = null;

    //if no one is currently working om this job
    if (!inquiry.isonworking) {

        inquiry.isonworking = true;
        ajaxRequest("/inquiryonworking", "PUT", inquiry);

        inqCodeInput.value = inquiry.inqcode;
        inqRecievedMethod.value = inquiry.recievedmethod;
        inqType.value = inquiry.inqtype;
        inqRecievedDate.value = inquiry.recieveddate;
        inqRecievedTime.value = inquiry.recievedtime;
        InqClientNationality.value = inquiry.nationality.countryname;
        inqClientTitle.value = inquiry.clienttitle
        inqClientName.value = inquiry.clientname
        inqClientPassportNumorNIC.value = inquiry.passportnumornic;
        inqContactOne.value = inquiry.contactnum;
        inqAdditionalContact.value = inquiry.contactnumtwo;
        inqPrefferedContactMethod.value = inquiry.prefcontactmethod;
        inqMainEnquiry.value = inquiry.enquiry;
        inqClientEmail.value = inquiry.email;
        inqNote.value = inquiry.note;
        inqPickupLoc.value = inquiry.pickuplocation;
        onqDropOffLock.value = inquiry.dropofflocation;
        inqArrivalDate.value = inquiry.arrivaldate;
        inqLocalAdultCountInput.value = inquiry.inqlocaladultcount;
        inqLocalChildCountInput.value = inquiry.inqlocalchildcount;
        inqForeignAdultCountInput.value = inquiry.inqforeignadultcount;
        inqForeignChildCountInput.value = inquiry.inqforeignchildcount;

        if (inquiry.isguideinclude) {
            guideYes.checked = true;
        } else {
            guideNo.checked = true;
        }

        inqInterestedPkg.disabled = false;
        if (inquiry.based_tpkg_id == null) {
            tourpkgs = ajaxGetRequest("/tourpackage/alldata");
            fillDataIntoSelect(inqInterestedPkg, 'Please Select The Interested package', tourpkgs, 'packagename');
            inqInterestedPkg.disabled = true;

        } else {
            tourpkgs = ajaxGetRequest("/tourpackage/alldata");
            fillDataIntoSelect(inqInterestedPkg, 'Please Select The Interested package', tourpkgs, 'packagename', inquiry.based_tpkg_id.packagename);
        }

        //me datalist eka refill karanne kohomada?💥💥💥
        // nationalityListEdit = ajaxGetRequest('/nationality/alldata')
        // fillDataIntoSelect(dataListNationality, `Please Select Client's Country`, nationalityListEdit, 'countryname', inquiry.nationality.countryname);
        // InqClientNationality

        // inqStatusList = ajaxGetRequest('/inqstatus/alldata')
        // fillDataIntoSelect(inqStatus, 'Select Status', inqStatusList, 'name', inquiry.inquirystatus.name);

        //inquiry eka manual damma ekak neme nam me fields employeelata edit karanna bari karanna hadanna💥 fields tika danna
        if (inquiry.recievedmethod == "Website") {
            inqMainEnquiry.disabled = true;
            inqRecievedMethod.disabled = true;
            inqType.disabled = true;
            inqRecievedDate.disabled = true;
            inqRecievedTime.disabled = true;
            inqInterestedPkg.disabled = true;
        }

        $('#modalInquiryResponse').modal('show');

        let responcesbyInquiry = ajaxGetRequest('/inquiryresponse/byinquiry/' + inquiry.id);

        // Get the container where the responses will be appended
        let responsesContainer = document.getElementById("submittedAllResponses");
        responsesContainer.innerHTML = '';

        responcesbyInquiry.forEach((response, index) => {

            // Main response row
            let responseRow = document.createElement("div");
            responseRow.className = "row";

            // Response label
            let responseLabel = document.createElement("div");
            responseLabel.className = "row form-label mb-0 mt-3";
            responseLabel.textContent = `Response #${index + 1}`;

            // Inner content row
            let contentRow = document.createElement("div");
            contentRow.className = "row mt-2";

            // Textarea column (col-6)
            let col6TextArea = document.createElement("div");
            col6TextArea.className = "col-6";

            let textarea = document.createElement("textarea");
            textarea.disabled = true;
            textarea.style.height = "100px";
            textarea.className = "w-100 form-control form-input";
            textarea.textContent = response.content;

            col6TextArea.appendChild(textarea);

            // Date, time, and employee info column (col-6)
            let col6Info = document.createElement("div");
            col6Info.className = "col-6";

            let dateTimeRow = document.createElement("div");
            dateTimeRow.className = "row";

            let dateTimeLabel = document.createElement("div");
            dateTimeLabel.className = "col-4 form-label";
            dateTimeLabel.textContent = "Date And Time:";

            let dateTimeValue = document.createElement("div");
            dateTimeValue.className = "col-8";
            dateTimeValue.innerText = response.addeddatetime.replace("T", "    ");

            dateTimeRow.appendChild(dateTimeLabel);
            dateTimeRow.appendChild(dateTimeValue);

            let employeeRow = document.createElement("div");
            employeeRow.className = "row mt-2";

            let employeeLabel = document.createElement("div");
            employeeLabel.className = "col-4 form-label";
            employeeLabel.textContent = "Employee:";

            let employeeValue = document.createElement("div");
            employeeValue.className = "col-8";
            let addedUser = ajaxGetRequest('/username/byid/' + response.addeduserid);
            employeeValue.textContent = addedUser.username;

            employeeRow.appendChild(employeeLabel);
            employeeRow.appendChild(employeeValue);

            col6Info.appendChild(dateTimeRow);
            col6Info.appendChild(employeeRow);

            // Append elements to the content row
            contentRow.appendChild(col6TextArea);
            contentRow.appendChild(col6Info);

            // Append response label and content row to the main response row
            responseRow.appendChild(responseLabel);
            responseRow.appendChild(contentRow);

            // Append the main response row to the responses container
            responsesContainer.appendChild(responseRow);
        });

        //left ayata right eke value eka awilla
        newResponce.inquiry_id = inquiry;

    } else {
        alert("This Inquiry is already In Progress. Please Choose Another");
    }
}

//website eken awa ewa harenna anith ewata edit denna.
const createNewResponseRecord = () => {

    createNewResponseRowBtn.disabled = true;

    // Create the main container div with class 'row mt-4'
    var responseContainer = document.createElement("div");
    responseContainer.className = "row mt-4";

    // Create the inner row div
    var innerRow = document.createElement("div");
    innerRow.classList = "row";

    // Create the first column (col-8)
    var col8 = document.createElement("div");
    col8.className = "col-8";
    // Create the label for the textarea
    var labelResponse = document.createElement("label");
    labelResponse.setAttribute("for", "inputNewResponseTextField");
    labelResponse.classList = "form-label mb-0 mt-3";
    labelResponse.textContent = "Response: ";
    // Create the textarea
    var textarea = document.createElement("textarea");
    textarea.id = "inputNewResponseTextField";
    textarea.style.height = "100px";
    textarea.className = "w-100 form-control form-input";
    textarea.setAttribute("onkeyup", "inputFieldValidator(this,'', 'newResponce' , 'content')");

    // Append label and textarea to the col-8 div
    col8.appendChild(labelResponse);
    col8.appendChild(textarea);

    // Create the second column (col-4)
    var col4 = document.createElement("div");
    col4.className = "col-4";

    // Create the label for the select
    var labelSelect = document.createElement("label");
    labelSelect.setAttribute("for", "newInqResponseStatusSelect");
    labelSelect.className = "form-label";
    labelSelect.textContent = "Inquiry Response:";

    // Create the select element
    var select = document.createElement("select");
    select.className = "form-control form-select";
    select.setAttribute("onchange", "selectDynamicVal(this,'','newResponce','inqResponseStatus')");
    // Create the options
    inqResponseStatuses = ajaxGetRequest('/inqresponsestatus/alldata')
    fillDataIntoSelect(select, 'Please Select The Status', inqResponseStatuses, 'name');

    // Append label and select to the col-4 div
    col4.appendChild(labelSelect);
    col4.appendChild(select);

    // Append the columns to the inner row div
    innerRow.appendChild(col8);
    innerRow.appendChild(col4);

    // Append the inner row to the main container
    responseContainer.appendChild(innerRow);

    // Append the entire response container to the submittedResponses div
    document.getElementById("submittedResponses").appendChild(responseContainer);

}

//this service will be needed for prevent 2 or more employees work on same inquiry at same time.
//implemented in inquirycontroller
const changeIsOnWorking = () => {
    ajaxRequest('/inquiry', 'PUT', inquiry)
}

//check errors in responses
const checkInqResponseErrors = () => {
    let errors = '';

    if (newResponce.content == null) {
        errors = errors + " Please Enter The Inquiry Response Summary \n";
    }
    if (newResponce.inqResponseStatus == null) {
        errors = errors + " Please Select The Inquiry Response Status \n";
    }

    return errors;
}

//meka wenas karanna
const checkMainInqInfoUpdates = () => {
    let updates = '';

    if (inquiry.inqtype != oldInquiry.inqtype) {
        updates = updates + " Inquiry Type has changed \n";
    }

    if (oldInquiry.based_tpkg_id != null) {
        if (inquiry.based_tpkg_id.packagename != oldInquiry.based_tpkg_id.packagename) {
            updates = updates + " Interested Package has changed \n";
        }
    }

    if (inquiry.recievedmethod != oldInquiry.recievedmethod) {
        updates = updates + " Inquiry source Type has changed \n";
    }

    if (inquiry.recievedcontactoremail != oldInquiry.recievedcontactoremail) {
        updates = updates + " Source Address has changed \n";
    }

    if (inquiry.recieveddate != oldInquiry.recieveddate) {
        updates = updates + " Recieved date has changed \n";
    }

    if (inquiry.recievedtime != oldInquiry.recievedtime) {
        updates = updates + "  Recieved time has changed \n";
    }

    if (inquiry.adultscount != oldInquiry.adultscount) {
        updates = updates + "Adult Traveller's count has changed \n";
    }

    if (inquiry.childcount != oldInquiry.childcount) {
        updates = updates + "Child Traveller's count has changed \n";
    }

    if (inquiry.enquiry != oldInquiry.enquiry) {
        updates = updates + " Package Type has changed \n";
    }

    if (inquiry.arrivaldate != oldInquiry.arrivaldate) {
        updates = updates + " Main Inquiry Text has changed \n";
    }

    if (inquiry.departuredate != oldInquiry.departuredate) {
        updates = updates + " Package Type has changed \n";
    }

    if (inquiry.pickuplocation != oldInquiry.pickuplocation) {
        updates = updates + " Pickup location has changed \n";
    }

    if (inquiry.dropofflocation != oldInquiry.dropofflocation) {
        updates = updates + " Dropoff location has changed \n";
    }

    if (inquiry.clientname != oldInquiry.clientname) {
        updates = updates + " Client Name has changed \n";
    }

    if (inquiry.nationality.countryname != oldInquiry.nationality.countryname) {
        updates = updates + " Client Nationality has changed \n";
    }

    if (inquiry.passportnum != oldInquiry.passportnum) {
        updates = updates + " Client Passport Number has changed \n";
    }

    if (inquiry.nic != oldInquiry.nic) {
        updates = updates + " Client NIC number has changed \n";
    }

    if (inquiry.email != oldInquiry.email) {
        updates = updates + " Package Type has changed \n";
    }

    if (inquiry.contactnum != oldInquiry.contactnum) {
        updates = updates + " Client Contact #1 has changed \n";
    }

    if (inquiry.contactnumtwo != oldInquiry.contactnumtwo) {
        updates = updates + " Client Contact #2 has changed \n";
    }

    if (inquiry.prefcontactmethod != oldInquiry.prefcontactmethod) {
        updates = updates + " Preferred contact method has changed \n";
    }

    if (inquiry.inquirystatus.name != oldInquiry.inquirystatus.name) {
        updates = updates + " Inquiry Status has changed \n";
    }


    return updates;
}

const sameContactError = () => {
    if (inqAdditionalContact.value == inqContactOne.value) {
        alert("Enter A Different Number From Previously Entered Contact Number")
        inqAdditionalContact.style.border = '2px solid red';
        inquiry.contactnumtwo = null
    } else {
        inputFieldValidator(this, '', 'inquiry', 'contactnumtwo');
        inqAdditionalContact.style.border = '2px solid lime';
    }
}

const updateInqAndResponses = () => {
    let mainInqUpdates = checkMainInqInfoUpdates();
    if (mainInqUpdates == '') {
        let inqResErrors = checkInqResponseErrors();
        if (inqResErrors == "") {
            let userConfirm = confirm("Are You Sure To Add New Responses?");
            if (userConfirm) {
                let postServiceResponseForInqResponses = ajaxRequest("/inquiryresponse", "POST", newResponce);
                if (postServiceResponseForInqResponses == "OK") {
                    let putServiceResponceForMainInq = ajaxRequest("/inquiry", "put", inquiry);
                    if (putServiceResponceForMainInq == "OK") {
                        alert("Inquiry New Updates and Response Successfully saved");
                    } else {
                        alert("An Error Occured " + putServiceResponceForMainInq);
                    }
                } else {
                    alert("An Error Occured " + postServiceResponseForInqResponses);
                }
                $('#modalInquiryResponse').modal('hide');
                refreshInquiryResponseForm();
                refreshInquiryResponseTable();
            } else {
                alert('Operation Cancelled By User');
            }
        } else {
            alert('Form Has Following Errors \n \n' + inqResErrors);
        }
    } else {
        let userResponse = confirm("Sure To Update ? \n \n " + mainInqUpdates);
        if (userResponse) {
            let putServiceResponceForMainInq = ajaxRequest("/inquiry", "put", inquiry);
            if (putServiceResponceForMainInq == "OK") {
                alert("Inquiry Updated Successfully");
            } else {
                alert("An Error Occured " + putServiceResponceForMainInq);
            }
        } else {
            alert('Operation Cancelled By User');
        }
    }
}

//get the existing clients
const filterClient = () => {
    let nicorppt = "";
    let nationalityVar = inquiry.nationality.countryname;
    console.log(nationalityVar);
    if (nationalityVar == "Sri Lanka") {
        if (new RegExp("^([0-9]{9}[VvXx])||([0-9]{12})$").test(inqClientPassportNumorNIC.value)) {
            nicorppt = inqClientPassportNumorNIC.value;
            console.log(nicorppt);
        }
    } else {
        if (new RegExp("^[A-Z0-9]{6,9}$").test(inqClientPassportNumorNIC.value)) {
            nicorppt = inqClientPassportNumorNIC.value;
        }
    }

    if (nicorppt != "") {
        let extClient = ajaxGetRequest("/client/byclietnicorppt/" + nicorppt);
        console.log(extClient);
        if (extClient != "") {

            inqClientName.value = extClient.clientname;
        }
    }

}


//save updates
// let putServiceResponceForMainInq = ajaxRequest("/inquiry", "put", inquiry);
// // let putServiceResponceForMainInq = ajaxRequest("/inquiry", "post", inquiry);
// if (putServiceResponceForMainInq == "OK") {
//     //responses save karaganna
//     let postServiceResponseForInqResponses = ajaxRequest("/inquiryresponse", "POST", newResponce);
//     if (postServiceResponseForInqResponses == "OK") {
//         alert("Inquiry New Updates and Response Successfully saved")
//         //response backend error nam
//     } else {
//         alert("An Error Occured " + postServiceResponseForInqResponses);
//     }
// } else {
//     alert("An Error Occured " + putServiceResponceForMainInq);
// }
