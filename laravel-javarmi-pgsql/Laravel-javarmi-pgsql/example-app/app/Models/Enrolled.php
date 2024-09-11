<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Enrolled extends Model
{
    use HasFactory;
    protected $table = 'enrolled_students';
    protected $fillable = ['student_id', 'course_id'];
}
