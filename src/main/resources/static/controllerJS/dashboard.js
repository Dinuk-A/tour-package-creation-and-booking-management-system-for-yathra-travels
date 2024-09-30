window.addEventListener('load', () => {

    // let newInqList = ajaxGetRequest("/inquiry/onlynew");
    // let newItemCount = newInqList.length;
    // document.getElementById('newInquiriesCount').innerText = newItemCount;

    let inProgressAndNewInqList = ajaxGetRequest("/inquiry/newandinprogressonly");
    let inProgressItemCount = inProgressAndNewInqList.length;
    document.getElementById('inProgressAndNewInqsCount').innerText = inProgressItemCount;

});


// function openNav() {
//     var sidebar = document.getElementById("dashboardSidebarID");
//     var mainArea = document.getElementById("mainAreaID");

//     if (sidebar.classList.contains("open")) {
//         sidebar.classList.remove("open");
//         sidebar.style.width = "0px";
//         mainArea.style.marginLeft = "0px";
//     } else {
//         sidebar.classList.add("open");
//         sidebar.style.width = "300px";
//         mainArea.setAttribute('style', 'margin-left: 300px;')
//     }
// }




