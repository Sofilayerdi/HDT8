package java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class MainJCF {
    public static void main(String[] args) {
        PriorityQueue<Paciente> colaPacientes = new PriorityQueue<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("pacientes.txt"))) {
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
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo de pacientes: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Sistema de Atención de Emergencias ===");
        System.out.println("Implementación con Java Collection Framework (PriorityQueue)");
        System.out.println("Presione Enter para atender al siguiente paciente o 'q' para salir.\n");
        
        while (!colaPacientes.isEmpty()) {
            System.out.println("[Pacientes en espera: " + colaPacientes.size() + "]");
            System.out.print("Siguiente paciente > ");
            
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) {
                break;
            }
            
            Paciente paciente = colaPacientes.poll();
            System.out.println("\nAtendiendo a: " + paciente);
            System.out.println("----------------------------------------");
        }
        
        if (colaPacientes.isEmpty()) {
            System.out.println("\nTodos los pacientes han sido atendidos.");
        } else {
            System.out.println("\nAtención interrumpida. Quedan " + colaPacientes.size() + " pacientes por atender.");
        }
        
        scanner.close();
    }
}