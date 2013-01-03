package intertigre.controllers

import intertigre.domain.Club
import intertigre.domain.ClubController

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import extension.custom.Report

@Report
class ClubControllerSpec extends BaseControllerSpec{

	ClubController controller = new ClubController()
	
	def 'crear un club'(){
		when:
			controller.params.horariosPreferidosParaLocal = horariosPreferidosParaLocal
			controller.params.nombre = 'Canottieri'
			controller.params.localidad = 'Tigre'
			controller.params.direccion = 'Emilio Mitre 123'
			controller.params.email = 'canottieri@gmail.com'
			controller.params.telefono = '4325'
			controller.params.triosDeCanchasDisponibles = 1
			controller.save()
			Club club = Club.first()
		then:
			Club.findAll().size() == 1
			club.horariosPreferidosParaLocal.containsAll(10, 22, 20)
		where:
			horariosPreferidosParaLocal << [' 10,20 , 22 ', '10,20,22', ' 10, 20 ,22']
	}
	
	def 'no deberia crear un club si los horarios estan fuera de rango'(){
		when:
			controller.params.horariosPreferidosParaLocal = horariosPreferidosParaLocal
			controller.params.nombre = 'Canottieri'
			controller.params.localidad = 'Tigre'
			controller.params.direccion = 'Emilio Mitre 123'
			controller.params.email = 'canottieri@gmail.com'
			controller.params.telefono = '4325'
			controller.params.triosDeCanchasDisponibles = 1
			controller.save()
		then:
			Club.findAll().size() == 0
			controller.flash.message == 'Ingresaste un numero mayor a 24 o menor a 0 para los horarios'
		where:
			horariosPreferidosParaLocal << [' 10,25 , 22 ', '-1,20,22', ' 10, 20 ,32']
	}
	
}
