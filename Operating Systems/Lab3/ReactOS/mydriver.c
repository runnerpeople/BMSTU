#include <ntddk.h>
#include <ntddbeep.h>
#ifndef NDEBUG
#define NDEBUG
#endif
#include <debug.h>

NTSTATUS NTAPI DriverEntry(IN PDRIVER_OBJECT DriverObject, IN PUNICODE_STRING RegistryPath);
VOID NTAPI BeepUnload(IN PDRIVER_OBJECT DriverObject);

NTSTATUS NTAPI DriverEntry(IN PDRIVER_OBJECT DriverObject, IN PUNICODE_STRING RegPath)
{
	SIZE_T Size=PAGE_SIZE*10;
	SIZE_T reqSize;
    	PVOID Addr = NULL;
    	PSYSTEM_PROCESS_INFORMATION ProcessInformation = NULL;
	DriverObject->DriverUnload = BeepUnload;
        DPRINT1("Driver George Ivanov has been uploaded\n");
	DPRINT1("Trying to get list of processes\n");
    	ZwAllocateVirtualMemory(NtCurrentProcess(),&Addr,0,&Size,MEM_RESERVE,PAGE_READWRITE);
    	Size = PAGE_SIZE * 5;
    	ZwAllocateVirtualMemory(NtCurrentProcess(),&Addr,0,&Size,MEM_COMMIT,PAGE_READWRITE);
    	ProcessInformation = Addr;
    	if (!ZwQuerySystemInformation(SystemProcessInformation,ProcessInformation, PAGE_SIZE, &reqSize)) {
        	ZwQuerySystemInformation(SystemProcessInformation,ProcessInformation, reqSize, NULL);
    	}
	DPRINT1("\n______________________________________\nProcess list:\n");
    	while (ProcessInformation->NextEntryOffset) {
        	DPRINT1("%S %d\n", ProcessInformation->ImageName.Buffer,ProcessInformation->UniqueProcessId);
        	ProcessInformation = (PSYSTEM_PROCESS_INFORMATION)((ULONG_PTR)ProcessInformation + ProcessInformation->NextEntryOffset);
    	}	
	DPRINT1("Process lst End\n______________________________________\n");
        return STATUS_SUCCESS;
}

VOID NTAPI BeepUnload(IN PDRIVER_OBJECT DriverObject) {
	PDEVICE_OBJECT DeviceObject;
	DPRINT1("STOP DRIVER");
	DeviceObject = DriverObject->DeviceObject;
        IoDeleteDevice(DeviceObject);
}

/* EOF */