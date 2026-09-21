export interface Owner { 
ownerid: number; 
firstname: string; 
lastname: string; 
} 

export interface Car { 
id: number; 
brand: string; 
model: string; 
color: string; 
registrationNumber: string; 
modelYear: number; 
price: number; 
owner: Owner; 
}