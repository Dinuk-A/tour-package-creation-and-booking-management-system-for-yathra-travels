window.addEventListener('load', () => {

    refreshYathraWebSite(openReusableModalForCardsReadMore);
    refreshGeneralInqForm();
    refreshPkgRelInqForm();

    nationalityList = ajaxGetRequest('/nationalityforweb/alldata');

    //get nationality list to general inq form
    fillDataIntoSelect(generalInqNationalitySelect, 'Select Nationality', nationalityList, 'countryname');

    //get nationality list to pkg related inq form
    fillDataIntoSelect(pkgRelInqNationalitySelect, 'Select Nationality', nationalityList, 'countryname');

})

//refresh whole website
const refreshYathraWebSite = (openReusableModalForCardsReadMore) => {

    nationalityList = ajaxGetRequest('/nationality/alldata')
    fillDataIntoDataListYathra(dataListNationality, nationalityList, 'countryname');
    fillDataIntoDataListYathra(dataListNationalityGeneral, nationalityList, 'countryname');

    packageList = ajaxGetRequest("/tourpackageforweb/alldata");
    packageCardsList.innerHTML = "";
    packageList.forEach(pkg => {

        // Create column div
        let columnDiv = document.createElement('div');
        columnDiv.className = "col-md-4 col-lg-3 mb-4";

        // Create card div
        let cardDiv = document.createElement('div');
        cardDiv.className = "card h-100 shadow-sm";

        // Create image tag
        let imageTag = document.createElement('img');
        imageTag.className = "card-img-top";
        if (pkg.img1 != null) {
            imageTag.src = atob(pkg.img1);
        } else {
            imageTag.src = 'resources/images/sigiriya.jpg';
        }
        imageTag.alt = 'Tour package image';

        // Create card body
        let cardBodyDiv = document.createElement('div');
        cardBodyDiv.className = "card-body d-flex flex-column";

        // Create card title
        let cardTitleHeadTag = document.createElement('h5');
        cardTitleHeadTag.className = "card-title";
        cardTitleHeadTag.innerText = pkg.packagename;

        // Create card text (short description)
        let cardParagraphTag = document.createElement('p');
        cardParagraphTag.className = "card-text";
        cardParagraphTag.innerText = pkg.description;

        // Create "Read more" button
        let cardReadMoreButton = document.createElement('button');
        cardReadMoreButton.setAttribute('type', 'button');
        cardReadMoreButton.className = 'btn btn-primary mt-auto';
        cardReadMoreButton.setAttribute('data-bs-toggle', 'modal');
        cardReadMoreButton.setAttribute('data-bs-target', '#reusableModalForCards');
        cardReadMoreButton.innerText = 'Read more';
        cardReadMoreButton.onclick = () => { openReusableModalForCardsReadMore(pkg); }

        // Append elements
        cardBodyDiv.appendChild(cardTitleHeadTag);
        cardBodyDiv.appendChild(cardParagraphTag);
        cardBodyDiv.appendChild(cardReadMoreButton);

        cardDiv.appendChild(imageTag);
        cardDiv.appendChild(cardBodyDiv);
        columnDiv.appendChild(cardDiv);

        packageCardsList.appendChild(columnDiv);
    });

}

//GENERAL INQ FORM
const refreshGeneralInqForm = () => {

    inquiry = new Object();

    generalInqForm.reset(); 
   
    inqClientName.style.border = "1px solid #ced4da";
    generalInqNationalitySelect.style.border = "1px solid #ced4da";
    inqClientEmail.style.border = "1px solid #ced4da";
    inqClientMobileOne.style.border = "1px solid #ced4da";
    prefContMethodGeneralForm.style.border = "1px solid #ced4da";
    inqClientEnquiries.style.border = "1px solid #ced4da";
    inqClientTitle.style.border = "1px solid #ced4da";

}

//PKG RELATED INQ FORM
const refreshPkgRelInqForm = () => {

    nationalityList = ajaxGetRequest('/nationalityforweb/alldata');
    fillDataIntoSelect(pkgRelInqNationalitySelect, 'Select Nationality', nationalityList, 'countryname');

    //for data list
    // fillDataIntoDataList(pkgrelatednationalityDataList, nationalityList, 'countryname');
}

//set country code auto
// const passCountryCodeInqForm = (selectElementID, cntctNumInputId) => {

//     const selectedCountry = JSON.parse(selectElementID.value).countrycode;
//     cntctNumInputId.value = selectedCountry;
// }

const passCountryCodeInqForm = (inputElement, cntctNumInput) => {
    const selectedCountryName = inputElement.value;
    const countryCode = countryData[selectedCountryName];

    if (countryCode) {
        cntctNumInput.value = countryCode;
    } else {
        // Handle the case where the country code is not found
        cntctNumInput.value = '';
    }
}

let countryData = {}; // Global object to store country data

const fillDataIntoDataListYathra = (fieldId, dataList, propertyName) => {
    fieldId.innerHTML = '';

    // Update the global countryData object
    countryData = {}; 

    for (const obj of dataList) {
        let option = document.createElement('option');
        option.value = obj[propertyName];
        fieldId.appendChild(option);

        // Store the country code in the global object
        countryData[obj[propertyName]] = obj.countrycode;
    }
}



//after clicking READ MORE btn
const openReusableModalForCardsReadMore = (package) => {

    // Clear previous content
    ForDPlansAccordions.innerHTML = "";
    pkgname.innerText = package.packagename;
    pkgDescription.innerText = package.description;

    //for images

    //img1 will be the cover photo...populated in js

    //img 2
    if (package.img2 != null) {
        image2.src = atob(package.img2);
    } else {
        image2.src = 'resources/images/sigiriya.jpg';
    }

    //img3
    if (package.img3 != null) {
        image3.src = atob(package.img3);
    } else {
        image3.src = 'resources/images/sigiriya.jpg';
    }

    //img1
    if (package.img4 != null) {
        image4.src = atob(package.img4);
    } else {
        image4.src = 'resources/images/sigiriya.jpg';
    }

    // For start dates
    accorsForStartDayPlans.appendChild(makeAccordionsForSDnED(package.sd_dayplan_id, 'Start Day ', "SD"));

    // Create accordions for middle day plans
    package.tourpkgHasDaypPlanList.forEach((tpDayPlan, index) => {

        //"accordion-item" div
        let accItemDiv = document.createElement('div');
        accItemDiv.className = "accordion-item";

        //acc header div , inside "accordion-item"
        let acHdrDiv = document.createElement('div');
        acHdrDiv.className = "accordion-header mb-2";

        //button inside header 
        let accBtn = document.createElement('button');
        accBtn.className = "p-0 accordion-button collapsed fw-bold mt-2";
        accBtn.setAttribute('type', 'button');
        accBtn.setAttribute('data-bs-toggle', 'collapse');
        accBtn.setAttribute('data-bs-target', '#flushCollapseOne' + index.toString());
        accBtn.setAttribute('aria-expanded', 'false');
        accBtn.setAttribute('aria-controls', 'flushCollapseOne' + index.toString());

        //h inside button
        let hTag = document.createElement('h5');
        hTag.className = "text-info";
        hTag.innerText = ("Day #" + (parseInt(index) + 2));

        //flushCollapseOne inside "accordion-item"
        let clpsDiv = document.createElement('div');
        clpsDiv.className = "accordion-collapse collapse ps-4 my-1";
        clpsDiv.setAttribute('id', 'flushCollapseOne' + index.toString());
        clpsDiv.setAttribute('data-bs-parent', 'ForDPlansAccordions');

        //inside flushCollapseOne 
        let accBody = document.createElement('div');
        accBody.className = "accordion-body bg-light ";
        let accBodyHTML = "";
        //create a list of points for each attraction of the day
        tpDayPlan.dayplan_id.vplaces.forEach((place, index) => {
            accBodyHTML = accBodyHTML + "<p class='mb-0'>" + (parseInt(index) + 1) + " ⏩ " + place.name + "</p>";
        });
        accBody.innerHTML = accBodyHTML;

        //append accordion-body to flushCollapseOne
        clpsDiv.appendChild(accBody);

        //append hTag to button
        accBtn.appendChild(hTag);

        //append button to accHeader 
        acHdrDiv.appendChild(accBtn);

        //append header and flushcollapseone to item
        accItemDiv.appendChild(acHdrDiv);
        accItemDiv.appendChild(clpsDiv);
        ForDPlansAccordions.appendChild(accItemDiv);
    });

    // For end dates
    //first check its existence(no end day in 1 day tours)
    if (package.ed_dayplan_id != null) {
        accorsForEndDayPlans.appendChild(makeAccordionsForSDnED(package.ed_dayplan_id, 'Last Day ', "ED"));
    }

    // Bind the package id with inquiry.based_tpkg_id field
    inquiry.based_tpkg_id = package;

    //show the selected pkg name in the form
    selectedPkgName.innerText = package.packagename;

    // Open the modal
    $('#reusableModalForCards').modal('show');
}


//common fn for create accordions for start day and end day
const makeAccordionsForSDnED = (dayPlan, dayType, SDorED) => {

    console.log("SD or ED Day plan");
    console.log(dayPlan);

    //"accordion-item" div
    let accItemDiv = document.createElement('div');
    accItemDiv.className = "accordion-item";

    //acc header div , inside "accordion-item"
    let acHdrDiv = document.createElement('div');
    acHdrDiv.className = "accordion-header mb-2";

    //button inside header 
    let accBtn = document.createElement('button');
    accBtn.className = "p-0 accordion-button collapsed fw-bold mt-2";
    accBtn.setAttribute('type', 'button');
    accBtn.setAttribute('data-bs-toggle', 'collapse');
    accBtn.setAttribute('data-bs-target', `#` + SDorED)
    accBtn.setAttribute('aria-expanded', 'false');
    accBtn.setAttribute('aria-controls', SDorED);

    console.log(dayType);

    //h inside button
    let hTag = document.createElement('h5');
    hTag.className = "text-info"
    hTag.innerText = dayType;
    // hTag.innerText = dayType + " " + dayPlan.daytitle;

    //flushCollapseOneSDorED inside "accordion-item"
    let clpsDiv = document.createElement('div');
    clpsDiv.className = "accordion-collapse collapse ps-4"
    clpsDiv.setAttribute('id', SDorED);
    clpsDiv.setAttribute('data-bs-parent', 'ForDPlansAccordions');

    //inside flushCollapseOneSDorED 
    let accBody = document.createElement('div');
    accBody.className = "accordion-body bg-light ";

    accBodyHTML = "";

    dayPlan.vplaces.forEach((place, index) => {
        accBodyHTML = accBodyHTML + "<p class='mb-0'>" + (parseInt(index) + 1) + " ⏩ " + place.name + "</p>";
    })
    accBody.innerHTML = accBodyHTML;

    //append accordion-body to flushCollapseOneSDorED
    clpsDiv.appendChild(accBody);

    //append hTag to button
    accBtn.appendChild(hTag);

    //append button to accHeader 
    acHdrDiv.appendChild(accBtn)

    //append header and flushcollapseoneSDorED to item
    accItemDiv.appendChild(acHdrDiv);
    accItemDiv.appendChild(clpsDiv);

    return accItemDiv;

}

//after clicking send inquiry btn
const openModalForPkgInqs = () => {
    $('#reusableModalForCards').modal('hide');
    $('#modalForPkgRelatedInqs').modal('show');

    //💥💥💥 + in html 197
    // document.getElementById('selectedPkgId').value = selectedpkg.id;
}

//ADD BTN OF PKG RELATED INQ FORM
const pkgRelInqAddBtn = () => {

    const errors = checkGenInqErrors();

    if (errors == '') {
        //GPT
        inquiry.inqtype = document.getElementById('inqTypeDummy').value;

        let postServiceResponse = ajaxRequest("/inquiry", "POST", inquiry)
        if (postServiceResponse == "OK") {
            alert("Thank you! Your inquiry has been submitted and we’ll get back to you soon");
            formPkgRelateInqForm.reset();
            refreshPkgRelInqForm();
            $("#modalForPkgRelatedInqs").modal("hide");
        } else {
            alert("Something went wrong while processing your request. Please try again later.");

        }
    } else {
        alert('Oops! There were some issues with your form submission:\n\n' + errors);
    }
}

//ADD BTN FOR GENERAL INQ FORM
const generalInqAddBtn = () => {

    const genInqErrors = checkGenInqErrors();

    if (genInqErrors == '') {

        let postServiceResponse = ajaxRequest("/inquirybycustomer", "POST", inquiry)
        if (postServiceResponse == "OK") {
            alert("Thank you! Your inquiry has been submitted and we’ll get back to you soon");
            generalInqForm.reset();
            refreshGeneralInqForm();
        } else {
            alert("Something went wrong while processing your request. Please try again later.");
        }

    } else {
        alert('Oops! There were some issues with your form submission:\n\n' + errors);

    }
}

//GENERAL INQ CHECK ERROR
const checkGenInqErrors = () => {
    let genInqFormErrors = '';

    if (!inquiry.clientname) {
        genInqFormErrors += "Please enter your name.\n";
    }
    if (!inquiry.nationality) {
        genInqFormErrors += "Please select your country.\n";
    }
    if (!inquiry.email) {
        genInqFormErrors += "Please provide your email address.\n";
    }
    if (!inquiry.contactnum) {
        genInqFormErrors += "Please enter your contact number.\n";
    }
    if (!inquiry.prefcontactmethod) {
        genInqFormErrors += "Please choose your preferred contact method.\n";
    }
    if (!inquiry.enquiry) {
        genInqFormErrors += "Please enter the details of your enquiry.\n";
    }

    return genInqFormErrors;
}


//days count eka anuwa FOREACH multiple accordions hadenna one💚🧡🧡

//first div tag
// let mainDiv = document.createElement('div');
// mainDiv.className = "mb-2 w-100 border p-3 accordion accordion-flush mt-1";
// mainDiv.setAttribute('id', $id);
//or mainDiv.id = ""
//id ekata placeholder ekak dala foreach day eka increment karanna💥💥💥

//second div
// let secondDiv = document.createElement('div');
// secondDiv.className = "accordion-item";

//div for acc header
// let divAccHdr = document.createElement('h5');
// divAccHdr.className = "accordion-header mb-2";

//button inside header
// let hdrBtn = document.createElement('button');
// hdrBtn.className = "p-0 accordion-button collapsed fw-bold ";
// hdrBtn.setAttribute('type', 'button');
// hdrBtn.setAttribute('data-bs-toggle', 'collapse');
// hdrBtn.setAttribute('data-bs-target', '#flushCollapseOne'); //mekath id ekak
// hdrBtn.setAttribute('aria-expanded', 'false');
// hdrBtn.setAttribute('aria-controls', 'flushCollapseOne'); //same id

//h for button txt
// let btnTxtH = document.createElement('h5');
// btnTxtH.className="text-info";
// btnTxtH.innerText='day___' //methentath placeholder value eka enna one

//div for actual accordion
// let accoDiv = document.createElement('div');
// accoDiv.className =  "accordion-collapse collapse ps-4";
// accoDiv.setAttribute('data-bs-parent' , '' ) ; //methana mulma id eke placeholder eka

//div for acc body
// let accBody = document.createElement('div');
// accBody.className="accordion-body bg-light d-flex justify-content-between";

//append all
// accoDiv.appendChild(accBody);
// divAccHdr.appendChild(hdrBtn);
// divAccHdr.appendChild(btnTxtH);
// secondDiv.appendChild(accoDiv);
// secondDiv.appendChild(divAccHdr);
// mainDiv.appendChild(secondDiv);
// divForAccordions.appendChild(mainDiv);


