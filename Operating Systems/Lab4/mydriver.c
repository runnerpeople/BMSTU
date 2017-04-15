#include <ntddk.h>
#include <ntddbeep.h>
#ifndef NDEBUG
#define NDEBUG
#endif
#include <debug.h>
#include <ntifs.h>
#include <mmtypes.h>
#include <exfuncs.h>
#include <ndk/exfuncs.h>

#define PTE_OFFSET 0xC0000000
#define PTE_addr(X) (PHARDWARE_PTE_X86)((ULONG)PTE_OFFSET + ((ULONG)((X)) >> 10))
#define PTE_next(X) (PHARDWARE_PTE_X86)((ULONG)((X)) + sizeof(PHARDWARE_PTE_X86))

NTSTATUS NTAPI DriverEntry(IN PDRIVER_OBJECT DriverObject, IN PUNICODE_STRING RegistryPath);
VOID NTAPI BeepUnload(IN PDRIVER_OBJECT DriverObject);

NTSTATUS NTAPI DriverEntry(IN PDRIVER_OBJECT DriverObject, IN PUNICODE_STRING RegPath)
{
	SIZE_T Size=PAGE_SIZE*10;
    	SIZE_T it;
	SIZE_T reqSize;
    	volatile SIZE_T access;
    	PVOID Addr = NULL;
    	PHARDWARE_PTE_X86 PPTE = NULL;
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
    	PPTE = PTE_addr(Addr);
    	for (it = 0; it < 10; it++) {
		if (it < 5) {
			access = *(PSIZE_T)((ULONG)Addr + PAGE_SIZE*it);
			DPRINT1("Access  = %X\n", access);
		}
        	DPRINT1("PTE %X:\n", it);
        	DPRINT1("Physaddr: %X\n", (PPTE->PageFrameNumber)<<12);
		DPRINT1("Valid: %d\n", PPTE->Valid);
        	PPTE = PTE_next(PPTE);
	}
    	ZwFreeVirtualMemory(NtCurrentProcess(),&Addr,&Size,MEM_DECOMMIT);
    	Size = 0;
    	ZwFreeVirtualMemory(NtCurrentProcess(),&Addr,&Size,MEM_RELEASE);
	MmPageEntireDriver(DriverEntry);
	DPRINT1("Done!\n");
	return STATUS_SUCCESS;
}

VOID NTAPI BeepUnload(IN PDRIVER_OBJECT DriverObject) {
	PDEVICE_OBJECT DeviceObject;
	DPRINT1("STOP DRIVER");
	DeviceObject = DriverObject->DeviceObject;
        IoDeleteDevice(DeviceObject);
}

/* EOF */