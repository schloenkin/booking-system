async function loadBookings() {
    const bookingsContainer = document.getElementById("bookings");

    try {
        const response = await fetch("/api/bookings");
        const bookings = await response.json();

        bookingsContainer.innerHTML = "";

        bookings.forEach(booking => {
            const bookingElement = document.createElement("div");
            bookingElement.className = "booking-card";

            bookingElement.innerHTML = `
                <strong>Booking #${booking.id}</strong>
                <p>User ID: ${booking.userId}</p>
                <p>Service ID: ${booking.serviceId}</p>
                <p>Start: ${booking.startTime}</p>
                <p>End: ${booking.endTime}</p>
                <p>Status: ${booking.status}</p>
            `;

            bookingsContainer.appendChild(bookingElement);
        });
    } catch (error) {
        bookingsContainer.innerHTML = "<p>Could not load bookings.</p>";
    }
}

loadBookings();