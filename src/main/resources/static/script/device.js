function toggleDevice(button) {
    const deviceCard = button.closest('.device-card');
    const deviceIcon = deviceCard.querySelector('.device-icon');
    const deviceName = deviceCard.querySelector('p').textContent.toLowerCase();

    if (button.textContent === "Off") {
        button.textContent = "On";
        button.classList.remove("off");
        button.classList.add("on");

        if (deviceName.includes("fan")) {
            deviceIcon.classList.add("spin");
        } else if (deviceName.includes("air conditioner")) {
            deviceIcon.classList.add("fade-out");

            setTimeout(() => {
                deviceIcon.src = "img/ac_on.svg";
                deviceIcon.classList.remove("fade-out");
                deviceIcon.classList.add("fade-in");
            },500)
        } else if (deviceName.includes("light bulb")) {
            deviceIcon.classList.add("fade-out"); // Bắt đầu hoạt ảnh mờ dần

            setTimeout(() => {
                deviceIcon.src = "img/bulb_on.svg"; // Đổi sang SVG mới
                deviceIcon.classList.remove("fade-out");
                deviceIcon.classList.add("fade-in"); // Bắt đầu hoạt ảnh xuất hiện
            }, 500); // Đợi hiệu ứng mờ dần hoàn tất
        }
    } else {
        button.textContent = "Off";
        button.classList.remove("on");
        button.classList.add("off");

        if (deviceName.includes("fan")) {
            deviceIcon.classList.remove("spin");
        } else if (deviceName.includes("air conditioner")) {
            deviceIcon.classList.add("fade-out");

            setTimeout(() => {
                deviceIcon.src = "img/ac.svg";
                deviceIcon.classList.remove("fade-out");
                deviceIcon.classList.add("fade-in");
            },500)
        } else if (deviceName.includes("light bulb")) {
            deviceIcon.classList.add("fade-out"); // Bắt đầu hoạt ảnh mờ dần

            setTimeout(() => {
                deviceIcon.src = "img/bulb.svg"; // Trở về SVG cũ
                deviceIcon.classList.remove("fade-out");
                deviceIcon.classList.add("fade-in"); // Bắt đầu hoạt ảnh xuất hiện
            }, 500); // Đợi hiệu ứng mờ dần hoàn tất
        }
    }

    // Loại bỏ lớp fade-in sau khi hoạt ảnh hoàn tất để chuẩn bị cho lần nhấn tiếp theo
    setTimeout(() => {
        deviceIcon.classList.remove("fade-in");
    }, 1000); // Tổng thời gian của hoạt ảnh (mờ dần + xuất hiện)
}
