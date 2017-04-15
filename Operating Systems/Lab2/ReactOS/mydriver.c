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
	DriverObject->DriverUnload = BeepUnload;
        DPRINT1("Driver George Ivanov has been uploaded\n");
        return STATUS_SUCCESS;
}

VOID NTAPI BeepUnload(IN PDRIVER_OBJECT DriverObject) {
	PDEVICE_OBJECT DeviceObject;
	DPRINT1("STOP DRIVER");
	DeviceObject = DriverObject->DeviceObject;
        IoDeleteDevice(DeviceObject);
}

/* EOF */