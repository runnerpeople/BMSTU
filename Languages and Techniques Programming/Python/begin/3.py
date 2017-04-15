import tkinter

master = tkinter.Tk()

e = tkinter.Entry(master)
e.pack()

e.focus_set()

def callback():
    print(e.get())

b = tkinter.Button(master, text="get", width=10, command=callback)
b.pack()

tkinter.mainloop()

e = tkinter.Entry(master, width=50)
e.pack()

text = e.get()

def makeentry(parent, caption, width=None, **options):
    tkinter.Label(parent, text=caption).pack(side=tkinter.LEFT)
    entry = tkinter.Entry(parent, **options)
    if width:
        entry.config(width=width)
    entry.pack(side=tkinter.LEFT)
    return entry

user = makeentry(tkinter.parent, "User name:", 10)
password = makeentry(tkinter.parent, "Password:", 10, show="*")

content = tkinter.StringVar()
entry = tkinter.Entry(tkinter.parent, text=tkinter.caption, textvariable=content)

text = content.get()
content.set(text)
