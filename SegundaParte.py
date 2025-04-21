import simpy
import random
import numpy as np
import matplotlib.pyplot as plt
from collections import defaultdict


random.seed(10) 
SIM_TIME = 480  
PATIENT_ARRIVAL_MEAN = 5  
NUM_NURSES = 3
NUM_DOCTORS = 4
NUM_XRAY_MACHINES = 2
NUM_LAB_TECHNICIANS = 2

NURSE_HOURLY_WAGE = 30  
DOCTOR_HOURLY_WAGE = 100  
XRAY_MACHINE_HOURLY_COST = 50  
LAB_TECH_HOURLY_WAGE = 40 

wait_times_by_priority = defaultdict(list)
treatment_times_by_priority = defaultdict(list)
resource_utilization = {
    'nurses': 0,
    'doctors': 0,
    'xray': 0,
    'lab': 0
}

class Patient:
    def __init__(self, env, name, arrival_time):
        self.env = env
        self.name = name
        self.arrival_time = arrival_time
        self.priority = None
        self.triage_time = None
        self.doctor_time = None
        self.xray_time = None
        self.lab_time = None
        self.discharge_time = None
    
    def calculate_wait_time(self):
        return self.discharge_time - self.arrival_time
    
    def calculate_treatment_time(self):
        return (self.discharge_time - self.arrival_time) - (self.triage_time - self.arrival_time)

def patient_generator(env, nurses, doctors, xray_machines, lab_technicians):
    patient_count = 0
    while True:
        yield env.timeout(random.expovariate(1.0 / PATIENT_ARRIVAL_MEAN))
        patient_count += 1
        name = f"Paciente-{patient_count}"
        patient = Patient(env, name, env.now)
        env.process(patient_flow(env, patient, nurses, doctors, xray_machines, lab_technicians))

def patient_flow(env, patient, nurses, doctors, xray_machines, lab_technicians):
    with nurses.request() as req:
        start_wait = env.now
        yield req
        wait_time = env.now - start_wait
        resource_utilization['nurses'] += env.now - start_wait
        
        yield env.timeout(10)
        patient.triage_time = env.now
        
        patient.priority = random.randint(1, 5)
    
    with doctors.request(priority=patient.priority) as req:
        start_wait = env.now
        yield req
        wait_time = env.now - start_wait
        resource_utilization['doctors'] += env.now - start_wait
        
        doctor_time = max(5, 30 - (patient.priority * 3))
        yield env.timeout(doctor_time)
        patient.doctor_time = env.now
    
    if random.random() < 0.5:
        with xray_machines.request(priority=patient.priority) as req:
            start_wait = env.now
            yield req
            wait_time = env.now - start_wait
            resource_utilization['xray'] += env.now - start_wait
            
            xray_time = random.uniform(10, 20)
            yield env.timeout(xray_time)
            patient.xray_time = env.now
    
    if random.random() < 0.5:
        with lab_technicians.request(priority=patient.priority) as req:
            start_wait = env.now
            yield req
            wait_time = env.now - start_wait
            resource_utilization['lab'] += env.now - start_wait
            
            lab_time = random.uniform(15, 30)
            yield env.timeout(lab_time)
            patient.lab_time = env.now
    
    patient.discharge_time = env.now
    total_wait = patient.calculate_wait_time()
    treatment_time = patient.calculate_treatment_time()
    
    wait_times_by_priority[patient.priority].append(total_wait)
    treatment_times_by_priority[patient.priority].append(treatment_time)

def run_simulation():
    env = simpy.Environment()
    
    nurses = simpy.Resource(env, capacity=NUM_NURSES)
    doctors = simpy.PriorityResource(env, capacity=NUM_DOCTORS)
    xray_machines = simpy.PriorityResource(env, capacity=NUM_XRAY_MACHINES)
    lab_technicians = simpy.PriorityResource(env, capacity=NUM_LAB_TECHNICIANS)
    
    env.process(patient_generator(env, nurses, doctors, xray_machines, lab_technicians))
    
    env.run(until=SIM_TIME)
    
    return env

def calculate_statistics():
    avg_wait_times = {}
    for priority, times in wait_times_by_priority.items():
        avg_wait = np.mean(times) if times else 0
        avg_wait_times[priority] = avg_wait
    
    avg_treatment_times = {}
    for priority, times in treatment_times_by_priority.items():
        avg_treatment = np.mean(times) if times else 0
        avg_treatment_times[priority] = avg_treatment
    
    total_time = SIM_TIME
    utilization = {
        'nurses': (resource_utilization['nurses'] / (NUM_NURSES * total_time)) * 100,
        'doctors': (resource_utilization['doctors'] / (NUM_DOCTORS * total_time)) * 100,
        'xray': (resource_utilization['xray'] / (NUM_XRAY_MACHINES * total_time)) * 100,
        'lab': (resource_utilization['lab'] / (NUM_LAB_TECHNICIANS * total_time)) * 100
    }
    
    return avg_wait_times, avg_treatment_times, utilization

def plot_results(avg_wait_times, avg_treatment_times, utilization):
    priorities = sorted(avg_wait_times.keys())
    wait_times = [avg_wait_times[p] for p in priorities]
    
    plt.figure(figsize=(10, 5))
    plt.bar(priorities, wait_times, color='skyblue')
    plt.title('Tiempo promedio de espera por prioridad')
    plt.xlabel('Prioridad (1 = más urgente)')
    plt.ylabel('Minutos')
    plt.xticks(priorities)
    plt.grid(axis='y', linestyle='--', alpha=0.7)
    plt.savefig('wait_times_by_priority.png')
    plt.close()
    
    resources = list(utilization.keys())
    util_values = [utilization[r] for r in resources]
    
    plt.figure(figsize=(10, 5))
    plt.bar(resources, util_values, color='lightgreen')
    plt.title('Utilización de recursos (%)')
    plt.ylabel('Porcentaje de utilización')
    plt.ylim(0, 100)
    plt.grid(axis='y', linestyle='--', alpha=0.7)
    plt.savefig('resource_utilization.png')
    plt.close()

def calculate_costs():
    nurse_cost = (SIM_TIME / 60) * NUM_NURSES * NURSE_HOURLY_WAGE
    doctor_cost = (SIM_TIME / 60) * NUM_DOCTORS * DOCTOR_HOURLY_WAGE
    xray_cost = (SIM_TIME / 60) * NUM_XRAY_MACHINES * XRAY_MACHINE_HOURLY_COST
    lab_cost = (SIM_TIME / 60) * NUM_LAB_TECHNICIANS * LAB_TECH_HOURLY_WAGE
    
    total_cost = nurse_cost + doctor_cost + xray_cost + lab_cost
    
    return {
        'nurse_cost': nurse_cost,
        'doctor_cost': doctor_cost,
        'xray_cost': xray_cost,
        'lab_cost': lab_cost,
        'total_cost': total_cost
    }

def generate_report(avg_wait_times, avg_treatment_times, utilization, costs):
    report_data = {
        "configuracion": {
            "duracion_minutos": SIM_TIME,
            "intervalo_llegada_pacientes": PATIENT_ARRIVAL_MEAN,
            "recursos": {
                "enfermeras": NUM_NURSES,
                "doctores": NUM_DOCTORS,
                "rayos_x": NUM_XRAY_MACHINES,
                "tecnicos_lab": NUM_LAB_TECHNICIANS
            }
        },
        "resultados": {
            "tiempos_espera": {f"prioridad_{p}": f"{t:.1f} minutos" 
                              for p, t in sorted(avg_wait_times.items())},
            "tiempos_tratamiento": {f"prioridad_{p}": f"{t:.1f} minutos" 
                                   for p, t in sorted(avg_treatment_times.items())},
            "utilizacion_recursos": {k: f"{v:.1f}%" for k, v in utilization.items()}
        },
        "costos": {
            "enfermeras": f"${costs['nurse_cost']:.2f}",
            "doctores": f"${costs['doctor_cost']:.2f}",
            "rayos_x": f"${costs['xray_cost']:.2f}",
            "laboratorio": f"${costs['lab_cost']:.2f}",
            "total": f"${costs['total_cost']:.2f}"
        },
        "metricas_adicionales": {
            "pacientes_atendidos": sum(len(times) for times in wait_times_by_priority.values()),
            "tiempo_espera_promedio": f"{np.mean([t for times in wait_times_by_priority.values() for t in times]):.1f} minutos",
            "tiempo_tratamiento_promedio": f"{np.mean([t for times in treatment_times_by_priority.values() for t in times]):.1f} minutos"
        }
    }
    
    # Guardar datos en formato JSON para fácil procesamiento
    import json
    with open('simulation_data.json', 'w') as f:
        json.dump(report_data, f, indent=4, ensure_ascii=False)
    
    # También imprimir en consola para visualización inmediata
    print("=== DATOS DE SIMULACIÓN ===")
    print(json.dumps(report_data, indent=4, ensure_ascii=False))
    
    return report_data

if __name__ == "__main__":
    env = run_simulation()
    
    avg_wait_times, avg_treatment_times, utilization = calculate_statistics()
    
    costs = calculate_costs()
    
    plot_results(avg_wait_times, avg_treatment_times, utilization)
    
    report = generate_report(avg_wait_times, avg_treatment_times, utilization, costs)
    print(report)