<?php

namespace App\Http\Controllers;

use App\Models\Enrolled;
use Illuminate\Http\Request;

class EnrolledController extends Controller
{
    public function index()
    {
        $enrolled = Enrolled::all();
        return view('Enrolled', compact('enrolled'));
    }
}