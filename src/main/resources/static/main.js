const userListUrl = 'http://localhost:8080/api/admin';
const userUrl = 'http://localhost:8080/api/user'
const rolesListUrl = 'http://localhost:8080/api/roles'
const createUserUrl = 'http://localhost:8080/api/admin';
let role;
let output = '';

fetch(userListUrl)
    .then(r => r.json())
    .then(data => listAllUsers(data))
const usersTable = document.getElementById('users-table')
const listAllUsers = (users) => {
    users.forEach(user => {
        role = '';
        console.log(user.roles)
        if (user.roles) {
            user.roles.forEach((r) => role += r.name.substring(5) + " ")
        }
        output +=
            `<tr>
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${role}</td>
                <td>
                    <button type="button" class="btn btn-info" data-toggle="modal"
                        data-target="#editModal" id="editButton" data-uid=${user.id}>Edit
                    </button>
                </td>
                <th>
                    <button type="button" class="btn btn-danger" data-toggle="modal"
                        data-target="#deleteModal" id="deleteButton" data-uid=${user.id}>Delete
                    </button>
                </th>
            </tr>`;
    });
    usersTable.innerHTML = output;
    console.log(users)
}

fetch(userUrl)
    .then(r => r.json())
    .then(data => userTable(data))

const userInfoAdmin = document.getElementById('about-user')
let userInfoOutput = '';
const userTable = (user) => {
    role = '';
    if (user.roles) {
        user.roles.forEach((r) => role += r.name.substring(5) + " ");
    }
    userInfoOutput = `
        <tr>
            <td>${user.id}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${role}</td>
        </tr>
     `
    userInfoAdmin.innerHTML = userInfoOutput;
}

usersTable.addEventListener('click', (e) => {
    e.preventDefault()
    if (e.target.id === 'editButton') {
        fetch(`http://localhost:8080/api/admin/${e.target.dataset.uid}`)
            .then(res => res.json())
            .then(data => {
                $('#idEdit').val(data.id)
                $('#firstnameEdit').val(data.firstName)
                $('#lastnameEdit').val(data.lastName)
                $('#ageEdit').val(data.age)
                $('#emailEdit').val(data.email)

                const xhr2 = new XMLHttpRequest();
                xhr2.open('GET', rolesListUrl);
                xhr2.onreadystatechange = () => {
                    if (xhr2.readyState === XMLHttpRequest.DONE) {
                        if (xhr2.status === 200) {
                            const data = JSON.parse(xhr2.responseText);
                            let options = '';
                            for (const {id, name} of data) {
                                options += `<option value="${id}">${name}</option>`;
                            }
                            selectRoleForm.innerHTML = options;
                            $('#rolesEdit').html(options);
                            $('#editModal').modal();
                        } else {
                            console.error('Error:', xhr2.status);
                        }
                    }

                };
                xhr2.send();
            });
    } else if (e.target.id === 'deleteButton') {
        fetch(`http://localhost:8080/api/admin/${e.target.dataset.uid}`)
            .then(res => res.json())
            .then(data => {
                role = '';
                data.roles.forEach((r) => role += r.name.substring(5) + " ")
                $('#idDelete').val(data.id)
                $('#firstnameDelete').val(data.firstName)
                $('#lastnameDelete').val(data.lastName)
                $('#ageDelete').val(data.age)
                $('#emailDelete').val(data.email)
                $('#rolesDelete').val(role)
                $('#deleteModal').modal()
            });
    }
});

const editModalForm = document.getElementById('editModalForm')
editModalForm.addEventListener('submit', (e) => {
    e.preventDefault()
    const firstnameById = document.getElementById('firstnameEdit')
    const lastnameById = document.getElementById('lastnameEdit')
    const ageById = document.getElementById('ageEdit')
    const emailById = document.getElementById('emailEdit')
    const passwordById = document.getElementById('passwordEdit')
    const roleById = document.getElementById('rolesEdit')

    const roles = [];
    for (let i = 0; i < roleById.options.length; i++) {
        if (roleById.options[i].selected) {
            roles.push({
                id: roleById.options[i].value,
                name: roleById.options[i].text
            });
        }
    }

    const requestBody = {
        id: document.getElementById('idEdit').value,
        firstName: firstnameById.value,
        lastName: lastnameById.value,
        age: ageById.value,
        email: emailById.value,
        password: passwordById.value,
        roles: roles
    };

    const uid = document.getElementById('idEdit').value
    fetch(`http://localhost:8080/api/admin/` + uid, {
        method: 'PATCH',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(res => console.log(res))
        .then(() => {
            $('#editModal').modal('hide')

            output = '';
            fetch(userListUrl)
                .then(res => res.json())
                .then(data => listAllUsers(data))
        })
})

const deleteModalForm = document.getElementById('deleteModalForm')
deleteModalForm.addEventListener('submit', (e) => {
    e.preventDefault()
    const uid = document.getElementById('idDelete').value
    fetch(`http://localhost:8080/api/admin/` + uid, {
        method: 'DELETE'
    })
        .then(res => console.log(res))
        .then(() => {
            $('#deleteModal').modal('hide')
            output = ''
            fetch(userListUrl)
                .then(res => res.json())
                .then(data => listAllUsers(data))
        })
})


const selectRoleForm = document.getElementById('roles');
loadRoles()

function loadRoles() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', rolesListUrl);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                const data = JSON.parse(xhr.responseText);
                let options = '';
                for (const {id, name} of data) {
                    options += `<option value="${id}">${name}</option>`;
                }
                selectRoleForm.innerHTML = options;
            } else {
                console.error('Error:', xhr.status);
            }
        }
    };
    xhr.send();
}

const createUserForm = document.getElementById('creating-user-form');
createUserForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const firstnameById = document.getElementById('firstname');
    const lastnameById = document.getElementById('lastname');
    const ageById = document.getElementById('age');
    const emailById = document.getElementById('email');
    const passwordById = document.getElementById('password');
    const roleById = document.getElementById('roles');


    const roles = [];
    for (let i = 0; i < roleById.options.length; i++) {
        if (roleById.options[i].selected) {
            roles.push({
                id: roleById.options[i].value,
                name: roleById.options[i].text
            });
        }
    }

    fetch('http://localhost:8080/api/admin', {
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify({
            firstName: firstnameById.value,
            lastName: lastnameById.value,
            age: ageById.value,
            email: emailById.value,
            password: passwordById.value,
            roles: roles
        })
    })
        .then(res => res.json())
        .then(data => {
            const dataArr = []
            dataArr.push(data)
            listAllUsers(dataArr)
            createUserForm.reset()
            $('[href="#nav-home"]').tab('show');
        })
        .catch(err => console.error(err));
})






