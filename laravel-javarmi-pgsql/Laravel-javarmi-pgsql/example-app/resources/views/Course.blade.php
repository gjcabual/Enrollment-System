<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Courses</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
    <div class="container-fluid">
        <div class="container">
            

            <div class="row pt-3 px-2 justify-content-center">
                <div class="col">
                    <ul class="nav nav-tabs">
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="#">Courses</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="students">Students</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link"  href="enrolled">Enrolled</a>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="row pt-3 px-2 justify-content-center">
                <div class="col">
                    <table class="table ">
                        <thead>
                            <tr>
                                <th>COURSE ID</th>
                                <th>COURSE CODE</th>
                                <th>COURSE DESCRIPTION</th>
                            </tr>
                        </thead>
                        <tbody>
                            @foreach($courses as $c)
                            <tr>
                                <td>{{ $c->id }}</td>
                                <td>{{ $c->name }}</td>
                                <td>{{ $c->description }}</td>
                            </tr>
                            @endforeach
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>