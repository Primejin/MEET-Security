/* 버튼 클릭 시 classList에 hidden(display: none;) 삭제하여 modal 보이기 */
function openCreate(){
	const openModal = document.querySelector(".create_modal");
	openModal.classList.remove("hidden");
};
function openJoin(){
	const openModal = document.querySelector(".join_modal");
	openModal.classList.remove("hidden");
};

/* modal의 취소 버튼 클릭 시 classList에 hidden(display: none;) 생성하여 안보이게 설정 */
function joinClose(){
	const closeModal = document.querySelector(".join_modal");
	closeModal.classList.add("hidden");
};
function createClose(){
	const closeModal = document.querySelector(".create_modal");
	closeModal.classList.add("hidden");
};

/* copy 아이콘 클릭시 input의 값 복사한 후 alert 띄우기 */
function copyText(){
	var copyText = document.getElementById("joinCode");
	copyText.select();
	copyText.setSelectionRange(0,9999);
	document.execCommand("copy");
	alert("복사되었습니다.");
}
