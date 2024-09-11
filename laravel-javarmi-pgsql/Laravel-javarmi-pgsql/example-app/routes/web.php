<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\StudentController;
use App\Http\Controllers\CourseController;
use App\Http\Controllers\EnrolledController;
use App\Models\Course;

Route::resource('courses', CourseController::class);

Route::get('/courses', [CourseController::class, 'index'])->name('courses.index');

Route::get('/students', [StudentController::class, 'index']);

Route::get('/enrolled', [EnrolledController::class, 'index']);


Route::get('/', function () {
    return view('Landing');
});
