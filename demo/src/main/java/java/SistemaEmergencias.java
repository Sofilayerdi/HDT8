package java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SistemaEmergencias {
    private PriorityQueue<Paciente> colaPacientes;

    public SistemaEmergencias() {
        colaPacientes = new VectorHeap<>();
    }

    public void cargarPacientes(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    String nombre = partes[0].trim();
                    String sintoma = partes[1].trim();
                    char codigo = partes[2].trim().charAt(0);
                    colaPacientes.add(new Paciente(nombre, sintoma, codigo));
                }
            }
        }
    }

    public void atenderPacientes() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sistema de Atención de Emergencias - Hospital UVG");
        System.out.println("Implementación con VectorHeap");
        System.out.println("Presione Enter para atender al siguiente paciente o 'q' para salir.");
        
        while (!colaPacientes.isEmpty()) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) {
                break;
            }
            
            Paciente paciente = colaPacientes.remove();
            System.out.println("Atendiendo a: " + paciente);
            System.out.println("Pacientes restantes: " + colaPacientes.size());
            System.out.println("Presione Enter para continuar...");
        }
        
        if (colaPacientes.isEmpty()) {
            System.out.println("Todos los pacientes han sido atendidos.");
        }
        scanner.close();
    }
}