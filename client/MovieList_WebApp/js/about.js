function openModal(title, image, description) {
            document.getElementById('modalTitle').textContent = title;
            document.getElementById('modalImage').src = image;
            document.getElementById('modalDescription').textContent = description;
            document.getElementById('movieModal').classList.remove('hidden');
        }
        function closeModal() {
            document.getElementById('movieModal').classList.add('hidden');
        }
        function toggleDarkMode() {
            document.body.classList.toggle('dark');
        }